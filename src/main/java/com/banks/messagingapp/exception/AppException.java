package com.banks.messagingapp.exception;

import org.springframework.http.HttpStatus;

public abstract class AppException extends RuntimeException {

	public AppException(String message) {
		super(message);
	}

	public abstract HttpStatus getHttpStatus();

}
