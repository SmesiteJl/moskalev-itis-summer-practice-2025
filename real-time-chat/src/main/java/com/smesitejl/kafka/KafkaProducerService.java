package com.smesitejl.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smesitejl.domain.dto.MessageDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void sendToModeration(MessageDTO dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send("messages.in", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
