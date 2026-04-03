package com.chatai.service;

import com.chatai.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final static Logger logger = LoggerFactory.getLogger(GeminiService.class);
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    @Retryable(
            value={Exception.class},
            maxAttempts = 2,
            backoff = @Backoff(3000)
    )
    public String askAI(String message) {

        String prompt = """
        You are an e-commerce assistant.
        Convert user input into STRICT JSON:

        {
          "action": "place_order | check_inventory | confirm_order",
          "product": "string",
          "quantity": number
        }

        Only return JSON. No explanation.
        if not found any JSON related data please response one line statement in text, such as chatting with the person,
        so user can agree to place order.(one line statement like chatting)
        User: %s
                """.formatted(message);
        
                Map<String, Object> request = Map.of(
                        "contents", List.of(
                                Map.of("parts", List.of(Map.of("text", prompt)))
                        )
                );

                logger.info(">>> print the request body {}", request);
                return webClient.post()
                        .uri(propertiesConfig.getAiApi())
                        .header("Content-Type", "application/json")
                        .header("X-goog-api-key", propertiesConfig.getApiKey())
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            }
}
