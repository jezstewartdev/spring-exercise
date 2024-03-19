package com.github.jezstewartdev.springexercise.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	
	@Value("${api.key}")
	private String apiKey;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public HttpEntity<String> httpEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", apiKey);
		return new HttpEntity<>(headers);
	}
	
	
}
