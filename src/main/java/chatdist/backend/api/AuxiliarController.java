package chatdist.backend.api;

import chatdist.backend.model.AuxMessage;
import chatdist.backend.model.DirectMessage;
import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;


@RestController
@EnableScheduling
@CrossOrigin(origins = "*")
public class AuxiliarController {


    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String EXCHANGE_NAME = "chat.dist.direct.exchange";
    private static final String ADMIN_ROUTING_KEY = "channel.general";
    private static boolean autoAck = true;
    private static String userName = "byfntbvj";
    private static String password = "2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE";
    private static String vHost = "byfntbvj";
    private static String hostName = "kangaroo.rmq.cloudamqp.com";
    private static int portNumber = 5672;
    private static String uri = "amqp://byfntbvj:2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE@kangaroo.rmq.cloudamqp.com/byfntbvj";
    private ConnectionFactory factory;
    private Connection conn;
    private Channel channel;


    @Autowired
    private SimpMessagingTemplate template;

    public AuxiliarController() throws IOException, TimeoutException
    {
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
        conn = factory.newConnection();
        channel = conn.createChannel();
        AMQP.Exchange.DeclareOk ok = channel.exchangeDeclare(EXCHANGE_NAME,"direct");
    }




    @MessageMapping("/chat-receive")
    public void receive(@Payload User user) throws Exception {
        System.out.println("Preparing reception in queue: " + user.getBindingName());
        channel.queueDeclare(user.getBindingName(), true, false, false, null);
        GetResponse response = channel.basicGet(user.getBindingName(), autoAck);
        do {
            if (response == null) {
                // System.out.println("To" + user.getUsername()+": Response is null. Wait a second.");
                Thread.sleep(1000);

            } else {
                AMQP.BasicProperties props = response.getProps();
                byte[] body = response.getBody();
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                System.out.println("To" + user.getUsername()+ ": " + (String) new String(body));

                this.template.convertAndSend("/topic/chat/" + user.getUsername(),(String) new String(body));
            }
            response = channel.basicGet(user.getBindingName(), autoAck);
        } while (true);
    }



    @MessageMapping("/chat-send")
    public @ResponseBody void sendMessage(@Payload DirectMessage message) throws IOException, TimeoutException {
        System.out.println("Calling send:");
        boolean end = false;

        // Creating Object of ObjectMapper define in Jakson Api
        ObjectMapper Obj = new ObjectMapper();

        try {

            // get Oraganisation object as a json string
            String jsonStr = Obj.writeValueAsString(message);

            // Displaying JSON String
            System.out.println(jsonStr);
            channel.basicPublish(EXCHANGE_NAME, message.getReceiver().getBindingName(), null, jsonStr.getBytes());
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(message);

    }


    @GetMapping("/receive")
    public @ResponseBody AuxMessage receive(@RequestParam("me") String receiver) throws IOException, TimeoutException {

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
        channel.queueDeclare(ADMIN_ROUTING_KEY, false, false, false, null);

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
                channel.basicPublish("", ADMIN_ROUTING_KEY, null, message.getBytes());
                System.out.println(" [x] Enviado '" + message + "'");
            }
        } while (!end);

        channel.close();
        conn.close();
    }

}
