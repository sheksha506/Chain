package com.example.message.consumer;

import com.example.message.entity.Message;
import com.example.message.repo.MessageRepository;
import com.example.message.service.ConnectedUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ConnectedUserService connectedUserService;

    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void consume(String messageJson) {
        try {
            Message message = objectMapper.readValue(messageJson, Message.class);
            message.setDelivered(false);
            Message savedMessage = messageRepository.save(message);

            System.out.println("KafkaConsumer saved message: " + savedMessage);

            // If receiver currently connected, send via websocket
            if (connectedUserService.isUserConnected(savedMessage.getReceiver())) {
                simpMessagingTemplate.convertAndSendToUser(
                        savedMessage.getReceiver(),
                        "/queue/chat",
                        savedMessage
                );
                savedMessage.setDelivered(true);
                messageRepository.save(savedMessage);
                System.out.println("Delivered via WebSocket to: " + savedMessage.getReceiver());
            } else {
                System.out.println("Receiver not connected: " + savedMessage.getReceiver());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
