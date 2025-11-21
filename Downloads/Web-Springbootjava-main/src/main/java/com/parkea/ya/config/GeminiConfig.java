package com.parkea.ya.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GeminiConfig {
    
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    
    @Value("${gemini.api.timeout:30000}")
    private int timeout;
    
    @Bean
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl(geminiApiUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
    
    public String getApiKey() {
        return geminiApiKey;
    }
    
    public String getApiUrl() {
        return geminiApiUrl;
    }
    
    public int getTimeout() {
        return timeout;
    }
}