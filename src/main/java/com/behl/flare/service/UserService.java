package com.behl.flare.service;

import com.behl.flare.dto.UserResponse;
import com.behl.flare.entity.User;
import com.behl.flare.mappers.UserMapper;
import com.behl.flare.repository.UserJpaRepository;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.behl.flare.client.FirebaseAuthClient;
import com.behl.flare.dto.TokenSuccessResponseDto;
import com.behl.flare.dto.UserCreationRequest;
import com.behl.flare.dto.UserLoginRequestDto;
import com.behl.flare.exception.AccountAlreadyExistsException;
import com.behl.flare.exception.InvalidLoginCredentialsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord.CreateRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

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
     * @throws IllegalArgumentException      if provided argument is {@code null}
     * @throws AccountAlreadyExistsException If an account with the provided email-id already exists.
     */
    @Transactional
    @SneakyThrows
    public void create(@NonNull final UserCreationRequest userCreationRequest) {
        final var request = new CreateRequest();
        request.setEmail(userCreationRequest.getEmail());
        request.setPassword(userCreationRequest.getPassword());
        request.setEmailVerified(Boolean.TRUE);

        UserRecord userRecord;
        try {
            userRecord = firebaseAuth.createUser(request);


//			ListUsersPage listUsersPage = firebaseAuth.listUsers(null);
//			int q = 0;

        } catch (final FirebaseAuthException exception) {
            if (exception.getMessage().contains("EMAIL_EXISTS")) {
                throw new AccountAlreadyExistsException("Account with provided email-id already exists");
            }
            throw exception;
        }

        User user = new User();
        user.setFirebaseId(userRecord.getUid());
        user.setEmail(userCreationRequest.getEmail());
        user.setDisplayName(userCreationRequest.getDisplayName());
        user.setPhoneNumber(userCreationRequest.getPhoneNumber());
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
}
