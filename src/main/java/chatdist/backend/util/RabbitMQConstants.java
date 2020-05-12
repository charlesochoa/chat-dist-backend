package chatdist.backend.util;

public class RabbitMQConstants {
    public static final String EXCHANGE_NAME = "chat.dist.direct.exchange";
    public static final String ADMIN_ROUTING_KEY = "channel.general";
    public static boolean AUTO_ACK = true;
    public static String HOST_NAME = "kangaroo.rmq.cloudamqp.com";
    public static int PORT_NUMBER = 5672;
}
