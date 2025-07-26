package com.smesitejl.service;


import com.smesitejl.domain.dto.MessageDTO;
import com.smesitejl.domain.entity.MessageEntity;
import com.smesitejl.domain.model.MessageType;
import com.smesitejl.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public MessageDTO save(MessageDTO dto) {
        MessageEntity entity = new MessageEntity();

        entity.setSender(dto.getSender());
        entity.setContent(dto.getContent());
        entity.setType(MessageType.valueOf(String.valueOf(dto.getType())));
        entity.setTimestamp(LocalDateTime.now());
        entity.setAttachmentUrl(dto.getAttachmentUrl());

        MessageEntity saved = repository.save(entity);

        dto.setTimestamp(saved.getTimestamp());
        return dto;
    }
}

