package com.smesitejl.ui.admin;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

@Route("admin")
public class AdminAnalyticsView extends VerticalLayout {

    private final AnalyticsFacadeService analyticsFacadeService;
    private final Grid<String[]> senderGrid = new Grid<>();
    private final Grid<String[]> wordsGrid = new Grid<>();

    @Autowired
    public AdminAnalyticsView(AnalyticsFacadeService analyticsFacadeService) {
        this.analyticsFacadeService = analyticsFacadeService;

        try {
            analyticsFacadeService.exportMessagesToCsv("messages.csv");
            List<Row> topSenders = analyticsFacadeService.getTopSenders("messages.csv", 5);
            List<Row> topWords = analyticsFacadeService.getTopWords("messages.csv", 10);

            senderGrid.addColumn(arr -> arr[0]).setHeader("Пользователь");
            senderGrid.addColumn(arr -> arr[1]).setHeader("Сообщений");
            senderGrid.setItems(topSenders.stream()
                    .map(row -> new String[]{row.getString(0), String.valueOf(row.getLong(1))}).toList());

            wordsGrid.addColumn(arr -> arr[0]).setHeader("Слово");
            wordsGrid.addColumn(arr -> arr[1]).setHeader("Частота");
            wordsGrid.setItems(topWords.stream()
                    .map(row -> new String[]{row.getString(0), String.valueOf(row.getLong(1))}).toList());

            add(senderGrid, wordsGrid);

        } catch (IOException e) {
            add("Ошибка при экспорте сообщений");
        }
    }
}
