package chatdist.backend.api;

import chatdist.backend.model.AuxMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;


@Controller
@EnableScheduling
@CrossOrigin(origins = "*")
public class AuxiliarController {


    private final static String QUEUE_NAME = "hola";
    private static boolean autoAck = true;
    private static String userName = "byfntbvj";
    private static String password = "2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE";
    private static String vHost = "byfntbvj";
    private static String hostName = "kangaroo.rmq.cloudamqp.com";
    private static int portNumber = 5672;
    private static String uri = "amqp://byfntbvj:2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE@kangaroo.rmq.cloudamqp.com/byfntbvj";
    private ConnectionFactory factory;


    @Autowired
    private SimpMessagingTemplate template;

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
//    @GetMapping("/")
//    public @ResponseBody
//    DirectMessage test() {
//        return new DirectMessage(new User("Charles","ing.charlesochoa@gmail.com"),new User("Gabriel","correopruebagabo@gmail.com"),null,"Saludo");
//    }



    @MessageMapping("/chat-send")
    public void sendMessage(@Payload AuxMessage message) throws Exception {
        for (int i=0; i<10;i++){
            Thread.sleep(1000);
            message.setMsg("message" + i);
            this.template.convertAndSend("/topic/chat" + message.getFrom(),message);
        }
    }

    @MessageMapping("/chat-receive")
    public void receive(@Payload AuxMessage message) throws Exception {

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        channel.queueDeclare(message.getFrom(), false, false, false, null);
        GetResponse response = channel.basicGet(message.getFrom(), autoAck);
        do {
            if (response == null) {

            } else {
                AMQP.BasicProperties props = response.getProps();
                byte[] body = response.getBody();
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                this.template.convertAndSend("/topic/chat",new ObjectMapper().readValue ((String) new String(body),AuxMessage.class));
            }
            response = channel.basicGet(message.getFrom(), autoAck);
        } while (true);
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
                completeRes += new String(body) +"_";

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
