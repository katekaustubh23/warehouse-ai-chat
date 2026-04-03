package com.chatai.controller;

import com.chatai.model.ChatRequest;
import com.chatai.model.ChatResponse;
import com.chatai.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatSocketController {

    private final ChatService chatService;
    /**
     * Client sends → /app/message
     * Server listens → @MessageMapping("/message")
     * Server sends → /topic/reply
     * Client subscribes → /topic/reply
     * */
    @MessageMapping("/message")
    @SendTo("/topic/message")
    public ChatResponse processMessage(ChatRequest message) throws JsonProcessingException {

        log.info("Received message from user:{}", message.getUserId());
        String aiResponse = chatService.process(message.getMessage(), message.getUserId());
        log.info("Final response : {}", aiResponse);
        String reply = "Server received: " + aiResponse;
        return new ChatResponse(message.getUserId(), reply);
    }
}
