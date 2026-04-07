package com.chatai.controller;

import com.chatai.model.ChatRequest;
import com.chatai.model.ChatResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatSocketController {

    /**
     * Client sends → /app/message
     * Server listens → @MessageMapping("/message")
     * Server sends → /topic/reply
     * Client subscribes → /topic/reply
     * */
    @MessageMapping("/message")
    @SendTo("/topic/message")
    public ChatResponse processMessage(ChatRequest message) throws InterruptedException {

        System.out.println("Received message from user: " + message.getUserId());
        Thread.sleep(5000);
        String reply = "Server received: " + message.getMessage();
        System.out.println("sending message: " + reply);
        return new ChatResponse(message.getUserId(), reply);
    }
}
