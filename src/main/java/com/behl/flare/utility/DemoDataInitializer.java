package com.behl.flare.utility;

import com.behl.flare.dto.UserCreationRequest;
import com.behl.flare.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Создатель демо-данных
 */
@Component
@ConditionalOnProperty(name = "application.generate-demo", havingValue = "true")
public class DemoDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);

    private final UserService userService;

    @Autowired
    public DemoDataInitializer(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void run(String... args) {
        try {
            generateDemoData();
        } catch (Exception e) {
            log.error("Error while adding demo data", e);
        }
    }


    /**
     * Генерация демо-данных (юзеры, карточки)
     * Включается при создании бина в конструкторе
     */
    private void generateDemoData() {
        String prefix = RandomStringUtils.randomAlphanumeric(4);
        for (int i = 0; i < 10; i++) {
            String name = prefix + "_" + i;
            String email = name + "@ngm.com";
            String phone = "+7" + RandomStringUtils.randomNumeric(10);
            UserCreationRequest request = new UserCreationRequest();
            request.setDisplayName(name);
            request.setEmail(email);
            request.setPassword(name);
            request.setPhoneNumber(phone);
            userService.create(request);
        }
    }



}
