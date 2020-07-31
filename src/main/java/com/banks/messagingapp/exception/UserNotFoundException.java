package com.banks.messagingapp.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class UserNotFoundException extends AppException {

	public UserNotFoundException(String message) {
		super(message);
	}

	@Override
	public HttpStatus getHttpStatus() {
		return NOT_FOUND;
	}
}
