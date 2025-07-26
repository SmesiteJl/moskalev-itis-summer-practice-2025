package com.smesitejl.service.analytics;

import org.apache.spark.sql.*;
import org.apache.spark.sql.functions;
import java.util.Arrays;
import java.util.List;

public class MessageAnalyticsService {

    private final SparkSession spark;

    public MessageAnalyticsService() {
        this.spark = SparkSession.builder()
                .appName("ChatAnalytics")
                .master("local[*]")
                .getOrCreate();
    }

    public List<Row> topSenders(String pathToCsv, int limit) {
        Dataset<Row> df = spark.read().option("header", true).csv(pathToCsv);
        return df.groupBy("sender")
                .count()
                .orderBy(functions.desc("count"))
                .limit(limit)
                .collectAsList();
    }

    public List<Row> topWords(String pathToCsv, int limit) {
        Dataset<Row> df = spark.read().option("header", true).csv(pathToCsv);
        Dataset<String> words = df.select("content")
                .as(Encoders.STRING())
                .flatMap((String s) ->
                                Arrays.stream(s.toLowerCase().split("\\W+"))
                                        .filter(w -> w.length() > 2)
                                        .iterator(),
                        Encoders.STRING());
        return words.groupBy("value")
                .count()
                .orderBy(functions.desc("count"))
                .limit(limit)
                .collectAsList();
    }
}
