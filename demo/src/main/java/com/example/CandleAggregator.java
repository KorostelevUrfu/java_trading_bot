package com.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
//Static Factory Method
public class CandleAggregator {
// преобразование минутных свечей в 5, 15 минут и 1 часовые свечи
    public static List<Candle> aggregateTo5Minutes(List<Candle> minuteCandles) {
        return aggregateCandles(minuteCandles, Duration.ofMinutes(5));
    }

    public static List<Candle> aggregateTo15Minutes(List<Candle> minuteCandles) {
        return aggregateCandles(minuteCandles, Duration.ofMinutes(15));
    }

    public static List<Candle> aggregateTo1Hour(List<Candle> minuteCandles) {
        return aggregateCandles(minuteCandles, Duration.ofHours(1));
    }

    private static List<Candle> aggregateCandles(List<Candle> minuteCandles, Duration duration) {
        List<Candle> aggregatedCandles = new ArrayList<>();

        if (minuteCandles.isEmpty()) {
            return aggregatedCandles; 
        }

        LocalDateTime currentTimestamp = minuteCandles.get(0).getTimestamp();
        double open = minuteCandles.get(0).getOpen();
        double high = 0;
        double low = 0;
        double close = 0;
        long volume = 0;

        for (Candle candle : minuteCandles) {
            if (Duration.between(currentTimestamp, candle.getTimestamp()).compareTo(duration) < 0) {
                high = Math.max(high, candle.getHigh());
                low = Math.min(low, candle.getLow());
                close = candle.getClose();
                volume += candle.getVolume();
            } else {
                //Создаем новую свечу
                aggregatedCandles.add(new Candle(currentTimestamp, open, high, low, close, volume));

                //Обновляем значение свечи
                currentTimestamp = candle.getTimestamp();
                open = candle.getOpen();
                high = candle.getHigh();
                low = candle.getLow();
                close = candle.getClose();
                volume = candle.getVolume();
            }
        }

        
        aggregatedCandles.add(new Candle(currentTimestamp, open, high, low, close, volume));

        return aggregatedCandles;
    }
}
