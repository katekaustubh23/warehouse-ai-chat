package com.chatai.controller;

import com.chatai.model.ChatRequest;
import com.chatai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/ai-chat")
public class ChatController {

    private final static Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;

    @PostMapping("/ask")
    public Map<String, String> chat(@RequestBody ChatRequest chatRequest) throws Exception {
        try {
            logger.info("Request from {} ", chatRequest.getUserId());
            String reply = chatService
                    .process(chatRequest.getMessage(), chatRequest.getUserId());
            return Map.of("rply", reply);
        }catch(Exception e){
            throw new Exception(e);
        }

    }
}
