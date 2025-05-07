package com.behl.flare.utility;

import com.behl.flare.dto.UserCreationRequest;
import com.behl.flare.entity.User;
import com.behl.flare.enums.Roles;
import com.behl.flare.repository.UserJpaRepository;
import com.behl.flare.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Создатель админской учетки
 */
@Component
//@ConditionalOnProperty(name = "application.generate-demo", havingValue = "true")
public class AdminAccountInitializer implements CommandLineRunner {


    private final UserJpaRepository userJpaRepository;

    @Value("${application.admin.email}")
    private String adminEmail;
    @Value("${application.admin.password}")
    private String adminPassword;
    @Value("${application.admin.name}")
    private String adminName;

    private static final Logger log = LoggerFactory.getLogger(AdminAccountInitializer.class);

    private final UserService userService;
    private final FirebaseAuth firebaseAuth;


    @Autowired
    public AdminAccountInitializer(
            UserService userService,
            FirebaseAuth firebaseAuth, UserJpaRepository userJpaRepository) {
        this.userService = userService;
        this.firebaseAuth = firebaseAuth;
        this.userJpaRepository = userJpaRepository;
    }


    @Override
    public void run(String... args) {
        try {
            checkFirebaseAdminCreated();
        } catch (Exception e) {
            log.error("Error while adding ADMIN user", e);
        }
    }


    public void checkFirebaseAdminCreated() {
        try {
            log.info("Firebase. Checking if Admin exists with email: {}", adminEmail);
            // Если юзера нет на Firebase - вылет исключения
            UserRecord userRecord = firebaseAuth.getUserByEmail(adminEmail);

            log.info("Firebase. Admin user is already exist!");
            log.info("Firebase. Admin user updating!");
            userService.updateUser(
                    adminEmail,
                    userRecord.getUid(),
                    adminName,
                    "+7" + RandomStringUtils.randomNumeric(10),
                    Roles.ROLE_ADMIN);
        } catch (FirebaseAuthException e) {
            log.info("Firebase. Admin user not found and will be created with email: {}", adminEmail);

            UserCreationRequest request = new UserCreationRequest();
            request.setDisplayName(adminName);
            request.setEmail(adminEmail);
            request.setPassword(adminPassword);
            request.setPhoneNumber("+7" + RandomStringUtils.randomNumeric(10));

            userService.createFirebaseUser(request, Roles.ROLE_ADMIN);
        }
    }


/*
    @Transactional
    public void checkLocalAdminCreated() {
        try {
            log.info("Local database. Checking if Admin exists with email: {}", adminEmail);

            userService.updateLocalUser(adminEmail, adminFirebaseId, )

            userJpaRepository.findByEmail(adminEmail)
                    .stream()
                    .findFirst()
                            .orElseGet()

            userJpaRepository.findByEmailAndRoleAndFirebaseId(adminEmail, Roles.ROLE_ADMIN, adminFirebaseId)
                    .stream()
                    .findFirst()
                    .orElseThrow();

            log.info("Local database. Admin user is already exist!");
        } catch (FirebaseAuthException e) {
            log.info("Local database. Admin user not found and will be created with email: {}", adminEmail);

            UserCreationRequest request = new UserCreationRequest();
            request.setDisplayName("ChitjaVerepanov");
            request.setEmail(adminEmail);
            request.setPassword(adminPassword);
            request.setPhoneNumber("+7" + RandomStringUtils.randomNumeric(10));

            User user = userService.createFirebaseUser(request);
            user.setRole(Roles.ROLE_ADMIN);
            userJpaRepository.save(user);
        }

    }
*/


}
