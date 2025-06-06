package com.behl.flare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserRoleViolationException extends ResponseStatusException {

	private static final long serialVersionUID = 3265020831437403636L;
	
	private static final String DEFAULT_MESSAGE = "Invalid Role change: User cannot change his role.";

	public UserRoleViolationException() {
		super(HttpStatus.FORBIDDEN, DEFAULT_MESSAGE);
	}

}