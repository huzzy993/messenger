package com.huzzy.messenger.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

public class DuplicateNicknameException extends AppException {

	public DuplicateNicknameException(String message) {
		super(message);
	}

	@Override
	public HttpStatus getHttpStatus() {
		return CONFLICT;
	}
}
