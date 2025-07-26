package com.smesitejl.controller.rest;

import com.smesitejl.domain.dto.MessageDTO;
import com.smesitejl.handler.ChatWebSocketHandler;
import com.smesitejl.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final FileStorageService storageService;
    private final ChatWebSocketHandler webSocketHandler;

    public FileUploadController(FileStorageService storageService, ChatWebSocketHandler webSocketHandler) {
        this.storageService = storageService;
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                         @RequestParam("sender") String sender) {
        try {
            String url = storageService.upload(file);

            String fileType = file.getContentType();
            String messageType = fileType != null && fileType.startsWith("image") ? "IMAGE" : "FILE";

            MessageDTO dto = new MessageDTO();
            dto.setSender(sender);
            dto.setContent("[Файл отправлен]");
            dto.setType(messageType);
            dto.setAttachmentUrl(url);
            dto.setTimestamp(LocalDateTime.now());

            webSocketHandler.broadcast(dto); // Минуем Kafka, это просто тест отправки

            return ResponseEntity.ok(url);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Ошибка загрузки файла");
        }
    }
}

