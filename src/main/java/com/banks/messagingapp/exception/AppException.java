package com.banks.messagingapp.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public abstract class AppException extends RuntimeException {

	public AppException(String message) {
		super(message);
	}

	public abstract HttpStatus getHttpStatus();

	public static class UserNotFoundException extends AppException {

		public UserNotFoundException(String message) {
			super(message);
		}

		@Override
		public HttpStatus getHttpStatus() {
			return NOT_FOUND;
		}
	}

	public static class DuplicateNicknameException extends AppException {

		public DuplicateNicknameException(String message) {
			super(message);
		}

		@Override
		public HttpStatus getHttpStatus() {
			return CONFLICT;
		}
	}

	public static class SameSenderAndRecipientException extends AppException {

		public SameSenderAndRecipientException() {
			super("Sender cannot be same as recipient");
		}

		@Override
		public HttpStatus getHttpStatus() {
			return BAD_REQUEST;
		}
	}

}
