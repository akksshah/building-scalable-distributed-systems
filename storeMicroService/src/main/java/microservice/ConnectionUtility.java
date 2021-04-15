package microservice;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtility {
    private static final ConnectionFactory factory = new ConnectionFactory();
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                factory.setHost("34.224.119.109");
//                factory.setHost("localhost");
                factory.setUsername("aakash");
                factory.setPassword("aakash");
                connection = factory.newConnection();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
