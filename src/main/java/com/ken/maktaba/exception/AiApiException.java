package com.ken.maktaba.exception;

public class AiApiException extends RuntimeException {

	private static final long serialVersionUID = 3581431459926312436L;

	public AiApiException(String message) {
		super(message);
	}

	public AiApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
