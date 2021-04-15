package dao;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class RabbitMqPurchaseConsumer {
    private static final int TOTAL_CONSUMERS = 1024 * 3 / 2;
//    private static final int TOTAL_CONSUMERS = 60;
    public static void main(String[] args) throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("34.224.119.109");
        factory.setUsername("aakash");
        factory.setPassword("aakash");
        final Connection connection = factory.newConnection();

        ExecutorService dbWorkers = Executors.newFixedThreadPool(60);
        for (int i = 0 ; i < TOTAL_CONSUMERS ; i++) {
            try {
                new RabbitMqDbDao(i, connection, dbWorkers).run();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
