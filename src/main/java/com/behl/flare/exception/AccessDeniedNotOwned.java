	package com.behl.flare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

    public class AccessDeniedNotOwned extends ResponseStatusException {

        private static final String DEFAULT_MESSAGE = "Access denied for not owned user changes.";

        public AccessDeniedNotOwned() {
            super(HttpStatus.FORBIDDEN, DEFAULT_MESSAGE);
        }

    }