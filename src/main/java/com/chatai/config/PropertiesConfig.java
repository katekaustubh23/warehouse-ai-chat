package com.chatai.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Configuration
public class PropertiesConfig {

    @Value("${gemini.api-url}")
    private String aiApi;

    @Value("${gemini.api-key}")
    private String apiKey;

}
