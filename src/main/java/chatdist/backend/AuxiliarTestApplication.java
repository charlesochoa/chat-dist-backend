package chatdist.backend;

import com.rabbitmq.client.*;
import org.springframework.boot.SpringApplication;

import java.util.Scanner;

public class AuxiliarTestApplication {

    private final static String QUEUE_NAME = "hola";
    private static boolean autoAck = false;
    private static String userName = "byfntbvj";
    private static  String password = "2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE";
    private static String vHost  = "byfntbvj";
    private static String hostName = "kangaroo.rmq.cloudamqp.com";
    private static int portNumber = 5672;
    private static String uri = "amqp://byfntbvj:2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE@kangaroo.rmq.cloudamqp.com/byfntbvj";


    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        SpringApplication.run(BackendApplication.class, args);
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setVirtualHost(vHost);
        factory.setHost(hostName);
        factory.setPort(portNumber);
        try {

            factory.setUri(uri);
        } catch (Exception e){
            System.out.println(e);
            System.exit(-1);
        }

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // Declaramos una cola en el broker a través del canal
        // recién creado llamada QUEUE_NAME (operación
        // idempotente: solo se creará si no existe ya)
        // Se crea tanto en el emisor como en el receptor, porque no
        // sabemos cuál se lanzará antes
        // Indicamos que no sea durable ni exclusiva
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Esperando mensajes. CTRL+C para salir");


        while (true) {
            GetResponse response = channel.basicGet(QUEUE_NAME, autoAck);
            if (response == null) {
                // No message retrieved.
            } else {
                AMQP.BasicProperties props = response.getProps();
                byte[] body = response.getBody();
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                String stringBody = new String(body);
                System.out.println(stringBody);
            }
        }
    }
}
