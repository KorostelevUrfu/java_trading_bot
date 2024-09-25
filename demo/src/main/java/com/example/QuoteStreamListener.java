package com.example;

import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.entities.Quote;
import ru.tinkoff.piapi.core.entities.CandleInstrument;
import ru.tinkoff.piapi.core.StreamingApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class QuoteStreamListener {
    private final DatabaseManager databaseManager;
    private final InvestApi investApi;

    public QuoteStreamListener(DatabaseManager databaseManager, InvestApi investApi) {
        this.databaseManager = databaseManager;
        this.investApi = investApi;

        databaseManager.createTableIfNotExists();
    }

    public void startStream() {
        StreamingApi streamingApi = investApi.getStreamingApi();
        List<String> tickers = Arrays.asList("SBER", "YDEX");
        streamingApi.subscribeQuotes(tickers);
        
        streamingApi.onQuoteEvent((ticker, quote) -> {
            saveQuote(ticker, quote);
        });
    }

    private void saveQuote(String ticker, Quote quote) {
        String insertSQL = "INSERT INTO quotes (instrument, price) VALUES (?, ?)";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, ticker);
            statement.setDouble(2, quote.getAsk().getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
