package com.banks.messagingapp.controller;

import com.banks.messagingapp.dto.ErrorDto;
import com.banks.messagingapp.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalControllerAdvice {

	@ExceptionHandler
	public ResponseEntity<ErrorDto> exceptionHandler(AppException appException) {
		return ResponseEntity.status(appException.getHttpStatus())
						.body(ErrorDto.builder().message(appException.getMessage()).build());
	}

	@ExceptionHandler
	public ResponseEntity<ErrorDto> exceptionHandler(MethodArgumentNotValidException exception) {
		return ResponseEntity.status(BAD_REQUEST)
						.body(ErrorDto.builder().message("Error " + exception.getMessage()).build());
	}

	@ExceptionHandler
	public ResponseEntity<ErrorDto> exceptionHandler(Exception exception) {
		return ResponseEntity.status(INTERNAL_SERVER_ERROR)
						.body(ErrorDto.builder().message("Server Error " + exception.getMessage()).build());
	}
}
