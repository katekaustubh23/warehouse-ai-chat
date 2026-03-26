package com.chatai.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PropertiesConfig {

    @Value("${gemini.api-url}")
    private String aiApi;

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${inventory.service.url}")
    private String inventoryUrl;

    @Value("${order.service.url}")
    private String orderUrl;

    @Value("${internal.security.key}")
    private String internalSecurityKey;
}
