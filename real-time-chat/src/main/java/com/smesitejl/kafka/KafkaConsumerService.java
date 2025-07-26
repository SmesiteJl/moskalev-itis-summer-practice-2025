package com.smesitejl.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smesitejl.domain.dto.MessageDTO;
import com.smesitejl.service.MessageService;
import com.smesitejl.handler.ChatWebSocketHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final MessageService messageService;
    private final ChatWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(MessageService messageService, ChatWebSocketHandler webSocketHandler) {
        this.messageService = messageService;
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "messages.out", groupId = "chat-app")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            MessageDTO dto = objectMapper.readValue(record.value(), MessageDTO.class);
            messageService.save(dto);
            webSocketHandler.broadcast(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
