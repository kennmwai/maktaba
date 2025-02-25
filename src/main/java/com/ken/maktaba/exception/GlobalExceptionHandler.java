package com.ken.maktaba.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ken.maktaba.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
		ErrorResponse errorResponseDto = new ErrorResponse();
		errorResponseDto.setTimestamp(LocalDateTime.now());
		errorResponseDto.setStatus(status.value());
		errorResponseDto.setMessage(message);
		return new ResponseEntity<>(errorResponseDto, status);
	}

	@ExceptionHandler(AiApiException.class)
	public ResponseEntity<ErrorResponse> handleAiApiException(AiApiException ex) {
		logger.error("AiApiException occurred: {}", ex.getMessage());
		return buildErrorResponse("AI Insights service error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		logger.error("Unexpected exception occurred: ", ex);
		return buildErrorResponse("An unexpected error occurred. Please try again later.",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> fieldErrors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

		ErrorResponse errorResponseDto = new ErrorResponse();
		errorResponseDto.setTimestamp(LocalDateTime.now());
		errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponseDto.setErrors(fieldErrors);
		errorResponseDto.setMessage("Validation failed for request");

		return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
	}

}