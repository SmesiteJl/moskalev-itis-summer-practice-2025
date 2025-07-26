package com.smesitejl.domain.dto;

import java.time.LocalDateTime;

public class MessageDTO {
    private String sender;
    private String content;
    private String type; // TEXT, IMAGE, FILE
    private String attachmentUrl;
    private LocalDateTime timestamp;

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}