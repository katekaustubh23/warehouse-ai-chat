package com.chatai.service;

import com.chatai.model.AIResponse;
import com.chatai.service.api.ClientServiceApis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final GeminiService geminiService;
    private final ClientServiceApis clientServiceApis;
    private final ObjectMapper mapper = new ObjectMapper();

    public String process(String message, String userId) {

        String aiRaw = geminiService.askAI(message);

        String json = extractJson(aiRaw);

        try {

            AIResponse response = mapper.readValue(json, AIResponse.class);

            return switch (response.getAction()) {

                case "check_inventory" ->clientServiceApis.checkInventory(response.getProduct());

                case "place_order" -> clientServiceApis.placeOrder(
                            userId,
                            response.getProduct(),
                            response.getQuantity()
                    );

                default -> "Sorry I didn't understand";
            };

        } catch (Exception e) {
            return "Error processing request";
        }

    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        return text.substring(start, end + 1);
    }
}
