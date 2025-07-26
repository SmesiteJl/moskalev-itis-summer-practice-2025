package com.smesitejl.service.analytics;


import com.smesitejl.chat.analytics.MessageAnalyticsService;
import com.smesitejl.repository.MessageRepository;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class AnalyticsFacadeService {

    private final MessageRepository messageRepository;
    private final MessageAnalyticsService analyticsService;

    public AnalyticsFacadeService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.analyticsService = new MessageAnalyticsService();
    }

    public void exportMessagesToCsv(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("sender,content\n");
            messageRepository.findAll().forEach(msg -> {
                try {
                    writer.write(String.format("\"%s\",\"%s\"\n",
                            msg.getSender().replace("\"", "'"),
                            msg.getContent().replace("\"", "'")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public List<Row> getTopSenders(String csvPath, int limit) {
        return analyticsService.topSenders(csvPath, limit);
    }

    public List<Row> getTopWords(String csvPath, int limit) {
        return analyticsService.topWords(csvPath, limit);
    }
}

