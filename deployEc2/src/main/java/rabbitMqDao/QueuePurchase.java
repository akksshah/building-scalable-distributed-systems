package rabbitMqDao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import util.Order;

public class QueuePurchase {
    private static final String EXCHANGE_NAME = "purchase_exchange";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Order order;
    private final RbmqChannelPool pool;

    public QueuePurchase(Order order, RbmqChannelPool pool) {
        this.order = order;
        this.pool = pool;
    }

    public void queuePurchase() {
        try {
            Channel channel = pool.getChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.basicPublish(EXCHANGE_NAME, "", null,
                                 mapper.writeValueAsString(order).getBytes());
            pool.returnChannel(channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
