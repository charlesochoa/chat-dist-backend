package chatdist.backend.api;

import chatdist.backend.BackendApplication;
import com.rabbitmq.client.*;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

@RestController
public class AuxiliarController {


    private final static String QUEUE_NAME = "hola";
    private static boolean autoAck = false;
    private static String userName = "byfntbvj";
    private static  String password = "2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE";
    private static String vHost  = "byfntbvj";
    private static String hostName = "kangaroo.rmq.cloudamqp.com";
    private static int portNumber = 5672;
    private static String uri = "amqp://byfntbvj:2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE@kangaroo.rmq.cloudamqp.com/byfntbvj";
    private ConnectionFactory factory;
    private Channel channel;
    private Connection conn;

    public AuxiliarController() throws IOException, TimeoutException {

        factory = new ConnectionFactory();
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

        conn = factory.newConnection();
        channel = conn.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

    }

    @GetMapping("/search")
    public String search(@RequestParam("q") String q) {
        return "HOLA " + q;
    }

    public void SendMessages() throws IOException, TimeoutException {

        int messageNumber;
        boolean end = false;

        do {
            System.out.println("Escribe un número y pulsa <Enter> para enviarlo. El 0 para finalizar.");
            Scanner sc = new Scanner(System.in);
            messageNumber = sc.nextInt();
            if (messageNumber == 0) {
                end = true;
            } else {
                Message message = "Mensaje: " + messageNumber;
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" [x] Enviado '" + message + "'");
            }
        } while (!end);

        channel.close();
        conn.close();
    }

    public void ReceiveMessages() throws IOException, TimeoutException {

        System.out.println(" [*] Esperando mensajes. CTRL+C para salir");


        channel.basicConsume(QUEUE_NAME, autoAck, "myConsumerTag",
            new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException
                {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    long deliveryTag = envelope.getDeliveryTag();
                    // (process the message components here ...)
                    String stringBody = new String(body);
                    System.out.println(stringBody);

                    channel.basicAck(deliveryTag, false);
                }
            });
    }
}