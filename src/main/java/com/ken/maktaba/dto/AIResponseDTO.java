package com.ken.maktaba.dto;

import java.util.List;

import lombok.Data;

@Data
public class AIResponseDTO {
	private String id;
	private String object;
	private long created;
	private String model;
	private String systemFingerprint;
	private List<Choice> choices;
	private Usage usage;

	public String getFirstMessage() {
		if (choices != null && !choices.isEmpty()) {
			return choices.get(0).getMessage().getContent();
		}
		return "No response from AI.";
	}

	@Data
	public static class Choice {
		private int index;
		private Message message;
		private Object logprobs; // Can be null
		private String finishReason;
	}

	@Data
	public static class Message {
		private String role;
		private String content;
	}

	@Data
	public static class Usage {
		private int promptTokens;
		private int completionTokens;
		private int totalTokens;
		private CompletionTokensDetails completionTokensDetails;
	}

	@Data
	public static class CompletionTokensDetails {
		private int reasoningTokens;
		private int acceptedPredictionTokens;
		private int rejectedPredictionTokens;
	}
}
