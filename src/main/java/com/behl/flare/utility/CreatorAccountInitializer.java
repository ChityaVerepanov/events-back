package com.behl.flare.utility;

import com.behl.flare.dto.UserCreationRequest;
import com.behl.flare.enums.Roles;
import com.behl.flare.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Создатель учетки CREATOR
 */
@Component
//@ConditionalOnProperty(name = "application.creator.email")
public class CreatorAccountInitializer implements CommandLineRunner {

    @Value("${application.creator.email}")
    private String creatorEmail;
    @Value("${application.creator.password}")
    private String creatorPassword;
    @Value("${application.creator.name}")
    private String creatorName;

    private static final Logger log = LoggerFactory.getLogger(CreatorAccountInitializer.class);

    private final UserService userService;
    private final FirebaseAuth firebaseAuth;


    @Autowired
    public CreatorAccountInitializer(
            UserService userService,
            FirebaseAuth firebaseAuth) {
        this.userService = userService;
        this.firebaseAuth = firebaseAuth;
    }


    @Override
    public void run(String... args) {
        try {
            if (StringUtils.isNotEmpty(creatorEmail)) {
                checkFirebaseCreatorCreated();
            }
        } catch (Exception e) {
            log.error("Error while adding CREATOR user", e);
        }
    }


    public void checkFirebaseCreatorCreated() {
        try {
            log.info("Firebase. Checking if CREATOR exists with email: {}", creatorEmail);
            // Если юзера нет на Firebase - вылет исключения
            UserRecord userRecord = firebaseAuth.getUserByEmail(creatorEmail);

            log.info("Firebase. CREATOR user is already exist!");
            log.info("Firebase. CREATOR user updating!");
            userService.updateUser(
                    creatorEmail,
                    userRecord.getUid(),
                    creatorName,
                    "+7" + RandomStringUtils.randomNumeric(10),
                    Roles.ROLE_CREATOR);
        } catch (FirebaseAuthException e) {
            log.info("Firebase. CREATOR user not found and will be created with email: {}", creatorEmail);

            UserCreationRequest request = new UserCreationRequest();
            request.setDisplayName(creatorName);
            request.setEmail(creatorEmail);
            request.setPassword(creatorPassword);
            request.setPhoneNumber("+7" + RandomStringUtils.randomNumeric(10));

            userService.createFirebaseUser(request, Roles.ROLE_CREATOR);
        }
    }

}
