package com.ken.maktaba.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolDto {
	private String type; // e.g., "function"
	private Map<String, Object> function; // Define a FunctionDto if the structure is complex
	// TODO: add other tool properties as needed
}