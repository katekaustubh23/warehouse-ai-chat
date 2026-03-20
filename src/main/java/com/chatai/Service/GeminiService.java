package com.chatai.Service;

import com.chatai.config.PropertiesConfig;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private PropertiesConfig propertiesConfig;
    private final WebClient webClient;

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
        User: %s
                """.formatted(message);
        
                Map<String, Object> request = Map.of(
                        "contents", List.of(
                                Map.of("parts", List.of(Map.of("text", prompt)))
                        )
                );
        
                return webClient.post()
                        .uri(propertiesConfig.getAiApi() + "?key=" + propertiesConfig.getApiKey())
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            }
}
