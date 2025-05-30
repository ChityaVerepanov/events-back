	package com.behl.flare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccessDeniedByRoleException extends ResponseStatusException {

	private static final long serialVersionUID = 2136966726322289087L;

	private static final String DEFAULT_MESSAGE = "Access denied for your role.";

	public AccessDeniedByRoleException() {
		super(HttpStatus.FORBIDDEN, DEFAULT_MESSAGE);
	}

}