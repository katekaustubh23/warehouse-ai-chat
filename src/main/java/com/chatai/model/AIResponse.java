package com.chatai.model;

import lombok.Data;

@Data
public class AIResponse {
    private String action;
    private String product;
    private Integer quantity;
}
