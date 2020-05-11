package chatdist.backend.api;

import chatdist.backend.model.AuxMessage;
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


    @PostMapping(path="/sign-up")
    public @ResponseBody
    User signUp(@RequestBody User user) throws IOException, TimeoutException {
        System.out.println("SIGNING UP");
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (!optionalUser.isPresent()) {
            channel.queueDeclare(user.getUsername(), false, false, false, null);
            User newUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()));
            channel.queueDeclare(newUser.getBindingName(),true,false,false,null);
            channel.queueBind(newUser.getBindingName(),EXCHANGE_NAME,newUser.getBindingName());
            channel.queueBind(newUser.getBindingName(),EXCHANGE_NAME,ADMIN_ROUTING_KEY);
            User savedUser = userRepository.save(newUser);
            return savedUser;
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Username already exists"
        );
    }


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
        channel.queueDeclare(ADMIN_ROUTING_KEY, false, false, false, null);
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
