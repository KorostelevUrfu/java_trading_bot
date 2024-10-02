package tinkoff;

import java.util.concurrent.CountDownLatch;


public class Main {

    public static void main(String[] args) {
        DataBase db = new DataBase();

        db.startDB();

        CandleStreamProcessor candleStreamProcessor = new CandleStreamProcessor(CreateToken.getToken());
        
        //Обработка и вывод значений свечей
        candleStreamProcessor.processMarketData(DataStorage.figiList);

        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread was interrupted: " + e.getMessage());
        }
        
    }
}