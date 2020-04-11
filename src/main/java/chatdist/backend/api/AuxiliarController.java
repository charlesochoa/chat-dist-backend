package chatdist.backend.api;

import chatdist.backend.BackendApplication;
import chatdist.backend.model.AuxMessage;
import chatdist.backend.model.Message;
import chatdist.backend.model.User;
import com.rabbitmq.client.*;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;


@RestController
@CrossOrigin(origins = "*")
public class AuxiliarController {


    private final static String QUEUE_NAME = "hola";
    private static boolean autoAck = false;
    private static String userName = "byfntbvj";
    private static String password = "2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE";
    private static String vHost = "byfntbvj";
    private static String hostName = "kangaroo.rmq.cloudamqp.com";
    private static int portNumber = 5672;
    private static String uri = "amqp://byfntbvj:2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE@kangaroo.rmq.cloudamqp.com/byfntbvj";
    private ConnectionFactory factory;

    public AuxiliarController() throws IOException, TimeoutException {

        factory = new ConnectionFactory();
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setVirtualHost(vHost);
        factory.setHost(hostName);
        factory.setPort(portNumber);
        try {

            factory.setUri(uri);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/")
    public @ResponseBody Message test() {
        return new Message(new User("Charles","ing.charlesochoa@gmail.com"),new User("Gabriel","correopruebagabo@gmail.com"),null,"Saludo");
    }



    @GetMapping("/search")
    public @ResponseBody String search(@RequestParam("q") String q) {
        return "Hello, " + q;
    }


    @GetMapping("/send")
    public @ResponseBody AuxMessage send(@RequestParam("to") String to, @RequestParam("from") String from, @RequestParam("msg") String msg) throws IOException, TimeoutException {
        System.out.println("Calling send");
        boolean end = false;

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = from + ": " + msg;
        channel.basicPublish("", to, null, message.getBytes());
        channel.close();
        conn.close();
        System.out.println("new AuxMessage(from,to,msg)");
        return new AuxMessage(from,to,msg);

    }


    @GetMapping("/receive")
    public @ResponseBody AuxMessage receive(@RequestParam("me") String receiver) throws IOException, TimeoutException {

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        channel.queueDeclare(receiver, false, false, false, null);
        String completeRes = "";
        GetResponse response = channel.basicGet(receiver, autoAck);
        do {
            if (response == null) {
                return new AuxMessage(null,null,completeRes);
            } else {
                AMQP.BasicProperties props = response.getProps();
                byte[] body = response.getBody();
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                completeRes += new String(body) +"\n";
            }
            response = channel.basicGet(receiver, autoAck);
        } while (true);
    }

    public void SendMessages() throws IOException, TimeoutException {
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        int messageNumber;
        boolean end = false;

        do {
            System.out.println("Escribe un número y pulsa <Enter> para enviarlo. El 0 para finalizar.");
            Scanner sc = new Scanner(System.in);
            messageNumber = sc.nextInt();
            if (messageNumber == 0) {
                end = true;
            } else {
                String message = "Mensaje extra: " + messageNumber;
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" [x] Enviado '" + message + "'");
            }
        } while (!end);

        channel.close();
        conn.close();
    }

}
