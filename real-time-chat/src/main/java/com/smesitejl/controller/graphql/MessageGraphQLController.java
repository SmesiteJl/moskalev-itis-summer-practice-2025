package com.smesitejl.controller.graphql;

import com.smesitejl.domain.dto.MessageDTO;
import com.smesitejl.domain.entity.MessageEntity;
import com.smesitejl.domain.model.MessageType;
import com.smesitejl.repository.MessageRepository;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MessageGraphQLController {

    private final MessageRepository repository;

    public MessageGraphQLController(MessageRepository repository) {
        this.repository = repository;
    }

    @QueryMapping
    public List<MessageDTO> messages() {
        return repository.findAll().stream().map(entity -> {
            MessageDTO dto = new MessageDTO();
            dto.setSender(entity.getSender());
            dto.setContent(entity.getContent());
            dto.setType(MessageType.valueOf(entity.getType().name()));
            dto.setTimestamp(entity.getTimestamp());
            dto.setAttachmentUrl(entity.getAttachmentUrl());
            return dto;
        }).toList();
    }
}
