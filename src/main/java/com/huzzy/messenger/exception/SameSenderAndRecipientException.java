package com.huzzy.messenger.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class SameSenderAndRecipientException extends AppException {

	public SameSenderAndRecipientException() {
		super("Sender cannot be same as recipient");
	}

	@Override
	public HttpStatus getHttpStatus() {
		return BAD_REQUEST;
	}
}
