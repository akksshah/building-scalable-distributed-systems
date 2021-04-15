package rabbitMqDao;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RabbitMqChannelFactory extends BasePooledObjectFactory<Channel> {
    private final Connection connection;

    @Override
    public Channel create() throws Exception {
        return connection.createChannel();
    }

    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }
}
