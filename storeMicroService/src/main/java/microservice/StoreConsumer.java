package microservice;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import lombok.AllArgsConstructor;
import lombok.Setter;
import util.Order;
import util.Utility;

@AllArgsConstructor
public class StoreConsumer implements Runnable {
    private final Map<Integer, Map<String, Integer>> storeInventory;
    private final Map<String, Map<Integer, Integer>> itemInventory;
    private static final String EXCHANGE_NAME = "purchase_exchange";
    private static final String QUEUE = "storeInventory";
    private final Connection connection;
    private final int count;
    private int counter;
    private final ExecutorService workers;
    @Setter
    private volatile boolean interrupt;

    @Override
    public void run() {
        try {
            Channel channel = connection.createChannel();
            channel.basicQos(1);
            boolean durable = false;
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            Map<String, Object> args = new HashMap<>();
            args.put("x-queue-mode", "lazy");
            channel.queueDeclare(QUEUE, durable, false, false, args);
            channel.queueBind(QUEUE, EXCHANGE_NAME, "");
            System.out.println("[*] Thread [" + count + "] ready to receive");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                if (interrupt) {
                    return;
                }
                String message = new String(delivery.getBody());
                counter += 1;
                System.out.println("    [" + counter + "] Received a message on thread [" + count + "]");
                Order purchaseOrder = Utility.getMapper().readValue(message, Order.class);
                workers.submit(new UpdateMap(purchaseOrder, storeInventory, itemInventory));
            };
            channel.basicConsume(QUEUE, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
