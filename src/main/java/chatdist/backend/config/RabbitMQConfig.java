package chatdist.backend.config;

import chatdist.backend.util.RabbitMQConstants;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
@Component
public class RabbitMQConfig {
    public static String username;
    public static String password;
    public static String vHost;
    public static String uri;

    @Value("${rabbitmq.username}")
    public void setUsername(String value) {
        this.username = value;
    }

    @Value("${rabbitmq.password}")
    public void setPassword(String value) {
        this.password = value;
    }

    @Value("${rabbitmq.vhost}")
    public void setVHost(String value) {
        this.vHost = value;
    }

    @Value("${rabbitmq.uri}")
    public void setUri(String value) {
        this.uri = value;
    }

    @Bean
    public Channel connect() throws IOException, TimeoutException {
        ConnectionFactory factory;
        Connection conn;
        Channel channel;
        factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(vHost);
        factory.setHost(RabbitMQConstants.HOST_NAME);
        factory.setPort(RabbitMQConstants.PORT_NUMBER);

        try {
            factory.setUri(uri);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
        conn = factory.newConnection();
        channel = conn.createChannel();
        AMQP.Exchange.DeclareOk ok = channel.exchangeDeclare(RabbitMQConstants.EXCHANGE_NAME,"direct");
        return channel;
    }
}
