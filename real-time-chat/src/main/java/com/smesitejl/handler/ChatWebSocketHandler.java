package com.smesitejl.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smesitejl.domain.dto.MessageDTO;
import com.smesitejl.kafka.KafkaProducerService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final KafkaProducerService kafkaProducer;
    private final ObjectMapper objectMapper;

    public ChatWebSocketHandler(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            MessageDTO dto = objectMapper.readValue(message.getPayload(), MessageDTO.class);
            kafkaProducer.sendToModeration(dto); // 🔄 Отправляем на модерацию
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcast(MessageDTO dto) {
        try {

            String contentHtml = objectMapper.writeValueAsString(dto);
            if (dto.getType().equals("IMAGE")) {
                contentHtml = "<img src='" + dto.getAttachmentUrl() + "' width='200'/>";
            } else if (dto.getType().equals("FILE")) {
                contentHtml = "<a href='" + dto.getAttachmentUrl() + "' target='_blank'>Download file</a>";
            } else {
                contentHtml = dto.getContent();
            }
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(json));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
