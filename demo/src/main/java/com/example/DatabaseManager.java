package com.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseManager {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseManager() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            this.url = properties.getProperty("db.url");
            this.username = properties.getProperty("db.username");
            this.password = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration properties");
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void createTableIfNotExists() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS quotes (" +
                    "id SERIAL PRIMARY KEY, " +
                    "instrument TEXT NOT NULL, " +
                    "price NUMERIC NOT NULL, " +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    public boolean checkMovingAverages(int period1, int period2) {
        List<Double> prices = fetchPricesFromDatabase();

        if (prices.size() < Math.max(period1, period2)) {
            return false;
        }

        double avg1 = prices.stream().skip(prices.size() - period1).limit(period1).mapToDouble(Double::doubleValue).average().orElse(0);
        double avg2 = prices.stream().skip(prices.size() - period2).limit(period2).mapToDouble(Double::doubleValue).average().orElse(0);

        return avg1 > avg2; // Возвращает true, если MA первого периода больше MA второго
    }

    private List<Double> fetchPricesFromDatabase() {
        List<Double> prices = new ArrayList<>();
        String selectSQL = "SELECT price FROM quotes ORDER BY id ASC";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                prices.add(resultSet.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prices;
        }
    }
}