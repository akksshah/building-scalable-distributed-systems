package dao;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import model.Purchase;
import util.Order;
import util.Utility;

public class RabbitMqDbDao implements Runnable {
    private static final String EXCHANGE_NAME = "purchase_exchange";
    private static final String QUEUE = "purchases";
    private final PurchaseTransaction DAO = new PurchaseTransaction();
    private final int count;
    private int counter = 0;
    private final Connection connection;
    private final ExecutorService workers;

    public RabbitMqDbDao(int count, Connection connection, ExecutorService workers) throws IOException, TimeoutException {
        this.count = count;
        this.connection = connection;
        this.workers = workers;
        System.out.println("Connection " + this.count + " active");
    }


    @Override
    public void run() {
        try {
            Channel channel = connection.createChannel();
            channel.basicQos(1);
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            Map<String, Object> args = new HashMap<>();
            args.put("x-queue-mode", "lazy");
            boolean durable = false;
            channel.queueDeclare(QUEUE, durable, false, false, args);
            channel.queueBind(QUEUE, EXCHANGE_NAME, "");
            System.out.println("[*] Thread [" + count + "] ready to receive");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                counter += 1;
                System.out.println("    [" + counter + "] Received a message on thread [" + count + "]");
                Order purchaseOrder = Utility.getMapper().readValue(message, Order.class);
//                System.out.println("        [...] Message converted");
//                try {
                workers.submit(new DatabaseDao(purchaseOrder));
//                    DAO.savePurchaseOrder(
//                            purchaseOrder
//                                    .getItems()
//                                    .stream()
//                                    .map(item ->
//                                                 new Purchase(
//                                                         purchaseOrder.getCustomerId(),
//                                                         purchaseOrder.getStoreId(),
//                                                         purchaseOrder.getDate(),
//                                                         item.getNumberOfItems(),
//                                                         item.getItemId())).collect(Collectors
//                                                         .toList()));
//                } catch (Exception e) {
//                    System.err.println("Save failed");
//                }
            };
            channel.basicConsume(QUEUE, true, deliverCallback, consumerTag -> {});
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
