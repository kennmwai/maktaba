package com.ken.maktaba.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChatCompletionRequest {
	private String model;
	private List<MessageDto> messages;
	private Double temperature;
	@JsonProperty("max_tokens")
	private Integer maxTokens;
	private Boolean stream;
	private Object stop;
//    @JsonProperty("top_p")
//    private Double topP;
//    @JsonProperty("random_seed")
//    private Integer randomSeed;
//    @JsonProperty("response_format")
//    private Map<String, String> responseFormat; // Assuming ResponseFormat is simple map, TODO: create dedicated class
//    private List<ToolDto> tools;
//    @JsonProperty("tool_choice")
//    private Object toolChoice; // Can be String or Map<String, String> (ToolChoice)
//    @JsonProperty("presence_penalty")
//    private Double presencePenalty;
//    @JsonProperty("frequency_penalty")
//    private Double frequencyPenalty;
//    @JsonProperty("n")
//    private Integer n;
//    private Map<String, String> prediction; // Assuming Prediction is simple map, TODO: create dedicated class
//    @JsonProperty("safe_prompt")
//    private Boolean safePrompt;

}