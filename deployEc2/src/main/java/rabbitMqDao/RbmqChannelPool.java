package rabbitMqDao;

import com.rabbitmq.client.Channel;

import org.apache.commons.pool2.ObjectPool;

import java.io.IOException;

public class RbmqChannelPool {
    private final ObjectPool<Channel> pool;

    public RbmqChannelPool(ObjectPool<Channel> pool) {
        this.pool = pool;
    }

    public Channel getChannel() throws IOException {
        Channel channel;
        try {
            channel = pool.borrowObject();
            return channel;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to borrow buffer from pool" + e.toString());
        }
    }

    public void returnChannel(Channel channel) throws Exception {
        if (channel != null) {
            pool.returnObject(channel);
        }
    }
}
