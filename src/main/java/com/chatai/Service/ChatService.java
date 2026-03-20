package com.chatai.Service;

import com.chatai.model.AIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final GeminiService geminiService;
    private final ObjectMapper mapper = new ObjectMapper();

    public String process(String message, String userId) {

        String aiRaw = geminiService.askAI(message);

        String json = extractJson(aiRaw);

        AIResponse ai;

        try {
            ai = mapper.readValue(json, AIResponse.class);
        } catch (Exception e) {
            return "Sorry, I didn't understand.";
        }
         /* switch (ai.getAction()) {

            case "check_inventory":
                return gatewayClient.checkInventory(ai.getProduct());

            case "place_order":
                return gatewayClient.placeOrder(userId, ai.getProduct(), ai.getQuantity());

            case "confirm_order":
                return gatewayClient.confirmOrder(userId);

            default:
                return "Invalid request";
        } */
        return toString();
    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        return text.substring(start, end + 1);
    }
}
