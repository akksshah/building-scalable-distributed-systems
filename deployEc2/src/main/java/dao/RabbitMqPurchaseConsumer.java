package dao;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqPurchaseConsumer {
    private static final int TOTAL_CONSUMERS = 2;
    public static void main(String[] args) {
        for (int i = 0 ; i < TOTAL_CONSUMERS ; i++) {
            try {
                new RabbitMqDbDao(i).run();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
