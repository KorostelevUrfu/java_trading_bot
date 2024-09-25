package com.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import ru.tinkoff.piapi.core.InvestApi;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        String token;
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            token = properties.getProperty("api.token");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration properties");
        }

        DatabaseManager databaseManager = new DatabaseManager();
        InvestApi investApi = InvestApi.create(token); 

        QuoteStreamListener quoteStreamListener = new QuoteStreamListener(databaseManager, investApi);
        quoteStreamListener.startStream();
    }
}
