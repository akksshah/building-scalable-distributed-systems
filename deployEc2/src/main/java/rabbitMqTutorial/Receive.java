package rabbitMqTutorial;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receive {
    private static final String QUEUE = "hello";
    private static final String EXCHANGE_NAME = "logs";

    private static void consumeMessageFromRabbitMq() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
//        factory.setHost("34.224.119.109");
//        factory.setUsername("aakash");
//        factory.setPassword("aakash");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//        String queueName = channel.queueDeclare().getQueue();
        String queueName = QUEUE;
        channel.queueDeclare(QUEUE, false, false, false, null);
        System.out.println("queueName: " + queueName);
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println(" [*] Waiting for messages. To exit press CMD+C");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        consumeMessageFromRabbitMq();
    }
}
