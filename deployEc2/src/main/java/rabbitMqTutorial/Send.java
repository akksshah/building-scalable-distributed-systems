package rabbitMqTutorial;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Deprecated
public class Send {
    private static final String QUEUE_NAME = "hello";
    private static final String EXCHANGE_NAME = "purchase_exchange";

    public static void main(String[] args) throws TimeoutException, IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
//        factory.setHost("34.224.119.109");
//        factory.setUsername("akks");
//        factory.setPassword("akks");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            for (int i = 0 ; i < 10 ; i++) {
                String s = i + " -> Message";
                channel.basicPublish(EXCHANGE_NAME, "", null, s.getBytes());
                System.out.println(" [x] Sent '" + s + "'");
            }
            channel.close();
        }
    }
    // publisher -> rbbitmq -> "Store" -> consumer[]
    // rabbitmq -> 1000 -> 2 consumer, prefetch (1) => consumers (will always have 1 message to
    // process)
}
