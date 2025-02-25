package com.ken.maktaba.util;

import com.ken.maktaba.dto.ChatCompletionRequest;

public interface AIService {
	String createChatCompletion(ChatCompletionRequest request);
}
