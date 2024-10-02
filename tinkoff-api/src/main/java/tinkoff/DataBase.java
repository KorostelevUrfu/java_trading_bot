package tinkoff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Timestamp;
import java.util.List;
import tinkoff.CandleStreamProcessor.CandleData;

public class DataBase {

    private final String db_url = String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s&ssl=%s",
        DataStorage.db_host, DataStorage.db_port, DataStorage.db_name, DataStorage.db_user, DataStorage.db_password, DataStorage.db_ssl);

    public void startDB() {
        try (Connection conn = DriverManager.getConnection(db_url);
             Statement state = conn.createStatement()) {

            createFigiTable(state, DataStorage.figiList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createFigiTable(Statement state, List<String> figiList) {
        for (String figi : figiList) {
            String table = String.format("CREATE TABLE IF NOT EXISTS %s (close_price REAL, time TIMESTAMP);", figi);
            try {
                state.execute(table);
                System.out.printf("Таблица успешно создана/существует для figi: %s.\n", figi);
            } catch (SQLException e) {
                System.err.printf("Ошибка при создании таблицы: %s\n", e.getMessage());
            }
        }
    }

    public void insertFigiData(CandleData candle) {
        String insertQuery = "INSERT INTO " + candle.getFigi() + " (close_price) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(db_url);
             PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {

            preparedStatement.setDouble(1, candle.getClosePrice());
            //preparedStatement.setTimestamp(2, Timestamp.valueOf(candle.getTime().toString())); // Используем Timestamp

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.printf("Данные свечи успешно добавлены в таблицу %s.\n", candle.getFigi());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
