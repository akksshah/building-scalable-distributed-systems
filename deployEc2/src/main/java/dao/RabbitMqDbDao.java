package dao;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import model.Purchase;
import util.Order;
import util.Utility;

public class RabbitMqDbDao implements Runnable {
    private static final String EXCHANGE_NAME = "purchase_exchange";
    private static final ConnectionFactory factory = new ConnectionFactory();
    private final Connection connection;
    private static final String QUEUE = "purchases";
    private static final PurchaseTransaction DAO = new PurchaseTransaction();
    private final int count;

    public RabbitMqDbDao(int count) throws IOException, TimeoutException {
        this.count = count;
//        factory.setHost("localhost");
                factory.setHost("34.224.119.109");
        factory.setUsername("akks");
        factory.setPassword("akks");
        connection = factory.newConnection();
        System.out.println("Connection " + this.count + " active");
    }


    @Override
    public void run() {
        try {
            Channel channel = connection.createChannel();
            channel.basicQos(1);
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.queueDeclare(QUEUE, false, false, false, null);
            channel.queueBind(QUEUE, EXCHANGE_NAME, "");
            System.out.println("[*] Thread ["+ count +"] ready to receive");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
//                System.out.println("    [x] Received a message on thread ["+ count +"]: " + message);
                Order purchaseOrder = Utility.getMapper().readValue(message, Order.class);
//                System.out.println("        [...] Message converted");
                try {
                    DAO.savePurchaseOrder(
                            purchaseOrder
                                    .getItems()
                                    .stream()
                                    .map(item ->
                                                 new Purchase(
                                                         purchaseOrder.getCustomerId(),
                                                         purchaseOrder.getStoreId(),
                                                         purchaseOrder.getDate(),
                                                         item.getNumberOfItems(),
                                                         item.getItemId())).collect(Collectors.toList()));
                } catch (Exception e) {
                    System.err.println("Save failed");
                }
            };
            channel.basicConsume(QUEUE, true, deliverCallback, consumerTag -> {});
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
