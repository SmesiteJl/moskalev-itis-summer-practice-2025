package com.smesitejl.domain.entity;

import com.smesitejl.domain.model.MessageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "messages")
@Getter
@Setter
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    private LocalDateTime timestamp;

    private String attachmentUrl;

}

