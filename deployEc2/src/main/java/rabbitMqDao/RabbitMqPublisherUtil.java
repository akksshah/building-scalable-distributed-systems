package rabbitMqDao;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.apache.commons.pool2.ObjectPool;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqPublisherUtil {
    private static final ConnectionFactory factory;
    private static Connection connection;

    private static ObjectPool<Channel> channelPool;

    static {
        factory = new ConnectionFactory();
        factory.setHost("34.224.119.109");
        factory.setUsername("akks");
        factory.setPassword("akks");
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static Connection getRabbitMqConnection() {
        return connection;
    }
}
