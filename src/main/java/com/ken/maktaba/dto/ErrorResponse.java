package com.ken.maktaba.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
	private LocalDateTime timestamp;
	private int status;
	private String message;
	private Map<String, String> errors;
}