package com.example.message.controller;

import com.example.message.entity.Message;
import com.example.message.producer.KafkaProducer;
import com.example.message.repo.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @MessageMapping("/send")
    public void sendMessage(@Payload Message message, Principal principal) {
        // principal must be non-null (set by handshake)
        String sender = principal != null ? principal.getName() : "unknown";
        message.setSender(sender);
        message.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
        message.setDelivered(false);

        messageRepository.save(message);
        kafkaProducer.sendMessage(message);
        System.out.println("Saved & produced message: " + message);
    }
}
