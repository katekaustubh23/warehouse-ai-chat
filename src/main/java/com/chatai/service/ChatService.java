package com.chatai.service;

import com.chatai.model.AIResponse;
import com.chatai.service.api.ClientServiceApis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final GeminiService geminiService;
    private final ClientServiceApis clientServiceApis;
    private final ObjectMapper mapper = new ObjectMapper();

    public String process(String message, String userId) throws JsonProcessingException {

        String aiRaw = geminiService.askAI(message);
        log.info("AI response : {} ", aiRaw);
        String json = extractJson(aiRaw);
        log.info("-> JSON RESPONE {} ", json);
        try {

            AIResponse response = mapper.readValue(json, AIResponse.class);

            String result = executionAction(response, userId);
            return result;
        } catch (Exception e) {
            return json;
        }

    }

    @Retryable(
            value={Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000)
    )
    private String executionAction(AIResponse response, String userId){
        try {
            return switch (response.getAction()) {

                case "check_inventory" ->clientServiceApis.checkInventory(response.getProduct());

                case "place_order" -> clientServiceApis.placeOrder(
                        userId,
                        response.getProduct(),
                        response.getQuantity()
                );

                default -> "Sorry I didn't understand";
            };
        }finally {
            return "services are down for placing order or inventory check";
        }
    }

    @Recover
    public String recover(Exception ex, AIResponse response, String userId) {

        log.error("All retries failed", ex);

        return "Services are currently unavailable. Please try again later.";
    }

    private String extractJson(String text) throws JsonProcessingException {
//        if(!text.contains("{")){
//            return text;
//        }
//        int start = text.indexOf("{");
//        int end = text.lastIndexOf("}");
//        return text.substring(start, end + 1);
        JsonNode root = mapper.readTree(text);

        JsonNode textNode = root
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text");
        return textNode.asText();
    }
}
