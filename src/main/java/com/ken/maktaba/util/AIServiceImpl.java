package com.ken.maktaba.util;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ken.maktaba.dto.AIResponseDTO;
import com.ken.maktaba.dto.ChatCompletionRequest;
import com.ken.maktaba.exception.AiApiException;

@Service
public class AIServiceImpl implements AIService {

	private static final Logger logger = LoggerFactory.getLogger(AIServiceImpl.class);

	@Value("${ai.api.endpoint}")
	private String apiEndpoint;

	@Value("${ai.api.model}")
	private String apiModel;

	@Value("${ai.api.stream}")
	private boolean apiStream;

	@Value("${ai.api.default-chat-temperature}")
	private double defaultChatTemperature;

	@Value("${ai.api.default-chat-max-tokens}")
	private int defaultChatMaxTokens;

	@Value("${ai.api.default-chat-stop-sequences}")
	private String defaultChatStopSequencesString;

	private final RestTemplate restTemplate;

	public AIServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public String createChatCompletion(ChatCompletionRequest request) {
		try {
			// Set default values from configuration if not provided in the request
			if (request.getModel() == null) {
				request.setModel(apiModel);
			}
			if (request.getTemperature() == null) {
				request.setTemperature(defaultChatTemperature);
			}
			if (request.getMaxTokens() == null) {
				request.setMaxTokens(defaultChatMaxTokens);
			}
			if (request.getStream() == null) {
				request.setStream(apiStream);
			}
			if (request.getStop() == null && defaultChatStopSequencesString != null) {
				// Parse comma-separated string to List<String> and set as stop sequences
				List<String> stopSequencesList = Arrays.asList(defaultChatStopSequencesString.split(","));
				request.setStop(stopSequencesList);
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<ChatCompletionRequest> requestEntity = new HttpEntity<>(request, headers);

			ResponseEntity<AIResponseDTO> response = restTemplate.postForEntity(apiEndpoint, requestEntity,
					AIResponseDTO.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				return response.getBody().getFirstMessage();
			} else {
				String errorMessage = String.format("OpenAI API request failed with status code: %s, body: %s",
						response.getStatusCode(), response.getBody());
				logger.error(errorMessage);
				throw new AiApiException(errorMessage);
			}

		} catch (RestClientException e) {
			String errorMessage = "Error calling OpenAI API: " + e.getMessage();
			logger.error(errorMessage);
			throw new AiApiException(errorMessage, e);
		} catch (Exception e) {
			String errorMessage = "Internal server error during OpenAI API call: " + e.getMessage();
			logger.error(errorMessage, e);
			throw new AiApiException(errorMessage, e);
		}
	}
}
