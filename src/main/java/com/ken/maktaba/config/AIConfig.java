package com.ken.maktaba.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AIConfig {

	@Bean
	RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, @Value("${ai.api.key}") String aiApiKey) {
		return restTemplateBuilder.additionalInterceptors((request, body, execution) -> {
			request.getHeaders().add("Authorization", "Bearer " + aiApiKey);
			return execution.execute(request, body);
		}).build();
	}
}
