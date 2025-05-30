package com.behl.flare.service;

import com.behl.flare.dto.user.UserRequest;
import com.behl.flare.dto.user.UserResponse;
import com.behl.flare.entity.User;
import com.behl.flare.enums.Roles;
import com.behl.flare.exception.UserRoleViolationException;
import com.behl.flare.mappers.UserMapper;
import com.behl.flare.repository.UserJpaRepository;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.behl.flare.client.FirebaseAuthClient;
import com.behl.flare.dto.TokenSuccessResponseDto;
import com.behl.flare.dto.user.UserCreationRequest;
import com.behl.flare.dto.user.UserLoginRequestDto;
import com.behl.flare.exception.AccountAlreadyExistsException;
import com.behl.flare.exception.InvalidLoginCredentialsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord.CreateRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

//    @Value("${application.generate-demo:false}")
//    private Boolean generateDemo;


    private final FirebaseAuth firebaseAuth;
    private final FirebaseAuthClient firebaseAuthClient;
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;


/*
    public UserService(
            FirebaseAuth firebaseAuth,
            FirebaseAuthClient firebaseAuthClient,
            UserJpaRepository userJpaRepository) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseAuthClient = firebaseAuthClient;
        this.userJpaRepository = userJpaRepository;

        if (Boolean.TRUE.equals(generateDemo)) {
            generateDemoData();
        }
    }
*/


    /**
     * Creates a new user record in the system corresponding to provided
     * creation request details.
     *
     * @param userCreationRequest the request containing user creation details
     * @param role                - роль юзера
     * @throws IllegalArgumentException      if provided argument is {@code null}
     * @throws AccountAlreadyExistsException If an account with the provided email-id already exists.
     */
    @Transactional
    @SneakyThrows
    public void createFirebaseUser(@NonNull final UserCreationRequest userCreationRequest, Roles role) {
        final var request = new CreateRequest();
        request.setEmail(userCreationRequest.getEmail());
        request.setPassword(userCreationRequest.getPassword());
        request.setEmailVerified(Boolean.TRUE);

        UserRecord userRecord;
        try {
            userRecord = firebaseAuth.createUser(request);
        } catch (final FirebaseAuthException exception) {
            if (exception.getMessage().contains("EMAIL_EXISTS")) {
                throw new AccountAlreadyExistsException("Account with provided email-id already exists");
            }
            throw exception;
        }

        // Создание/обновление локального юзера
        updateUser(
                userCreationRequest.getFileName(),
                userCreationRequest.getEmail(),
                userRecord.getUid(),
                userCreationRequest.getDisplayName(),
                userCreationRequest.getPhoneNumber(),
                role);
    }


    /**
     * Создание / обновление локального юзера
     */
    @Transactional
    public void updateUser(String filename, String email, String firebaseId, String displayName, String phoneNumber, Roles role) {
        // Создание/обновление локального юзера
        User user = userJpaRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseGet(User::new);
        if (StringUtils.isNotBlank(filename)) {
            user.setFileName(filename);
        }
        user.setFirebaseId(firebaseId);
        user.setEmail(email);
        user.setDisplayName(StringUtils.stripToEmpty(displayName));
        user.setPhoneNumber(StringUtils.stripToEmpty(phoneNumber));
        user.setRole(role);
        userJpaRepository.save(user);
    }


    /**
     * Validates user login credentials and generates an access token on successful
     * authentication.
     *
     * @param userLoginRequest the request containing user login details
     * @return a TokenSuccessResponseDto containing the access token
     * @throws IllegalArgumentException         if provided argument is {@code null}
     * @throws InvalidLoginCredentialsException If the provided login credentials are invalid.
     */
    public TokenSuccessResponseDto login(@NonNull final UserLoginRequestDto userLoginRequest) {
        return firebaseAuthClient.login(userLoginRequest);
    }


    /**
     * Получение списка юзеров с пейджингом
     */
    public Page<UserResponse> getUsersPageable(Pageable pageable) {
        Page<User> all = userJpaRepository.findAll(pageable);
        Page<UserResponse> response = all.map(userMapper::toResponse);
        return response;
    }


    /**
     * Получение пользователя (создание при необходимости)
     */
    @Transactional
    public User getUserOrCreate(String firebaseId, FirebaseToken firebaseToken) {
        return userJpaRepository.findByFirebaseId(firebaseId).orElseGet(() -> {
            User user = userMapper.toEntity(firebaseId, firebaseToken);
            return userJpaRepository.save(user);
        });
    }


    /**
     * Установка новой роли для юзера
     *
     * @param user
     * @param role
     */
    @Transactional
    public void setUserRole(User user, Roles role) {
        user.setRole(role);
    }


    /**
     * Получение текущего юзера
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }


    @Transactional
    public UserResponse getUserDetails() {
        User currentUser = getCurrentUser();
        return userMapper.toResponse(currentUser);
    }


    /**
     * Обновление юзера по локальному ID
     *
     * @param userId  - Идентификатор пользователя
     * @param request - DTO
     */
    @SneakyThrows
    @Transactional
    public void updateUserById(Long userId, @Valid UserRequest request) {
        // Текущий юзер не может менять себе роль
        Long currentUserId = getCurrentUser().getId();
        if (currentUserId.equals(userId)) {
            throw new UserRoleViolationException();
        }

        User user = userJpaRepository.findById(userId).orElseThrow();
//        user.setFirebaseId(request.getFirebaseId());
//        user.setEmail(request.getEmail());
//        user.setPhoneNumber(request.getPhoneNumber());
        user.setDisplayName(request.getDisplayName());
        user.setRole(request.getRole());
        user.setFileName(request.getFileName());
        userJpaRepository.save(user);
    }


}
