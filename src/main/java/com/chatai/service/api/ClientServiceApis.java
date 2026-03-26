package com.chatai.service.api;

import com.chatai.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClientServiceApis {

    private final PropertiesConfig propertiesConfig;

    private final WebClient webClient;

    /**
     * find the product available
     * @param product product value or name
     * */
    public String checkInventory(String product){

        return webClient.get()
                .uri(propertiesConfig.getInventoryUrl())
                .header("X-INTERNAL-KEY", propertiesConfig.getInternalSecurityKey())
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }

    public String placeOrder(String userId, String product, int quantity) {

        Map<String, Object> body = Map.of(
                "userId", userId,
                "product", product,
                "quantity", quantity
        );

        return webClient.post()
                .uri(propertiesConfig.getOrderUrl())
                .header("X-API-KEY", propertiesConfig.getInternalSecurityKey())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
