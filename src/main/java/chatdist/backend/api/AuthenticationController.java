package chatdist.backend.api;

import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(path="/auth")
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public AuthenticationController() throws IOException, TimeoutException
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
            User newUser = new User(user.getUsername(), user.getPassword());
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

    @PostMapping(path="/sign-in")
    public @ResponseBody
    Iterable<User> signIn(@RequestBody User user) throws IOException, TimeoutException {
        System.out.println("SIGNING IN");
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        System.out.println(user.getUsername());
        if (optionalUser.isPresent()) {
            System.out.println("User Found!");
            System.out.println(optionalUser.get().getPassword());
            System.out.println(passwordEncoder.encode(user.getPassword()));
            if(optionalUser.get().getPassword().equals( user.getPassword())){

                return userRepository.findAll() ;
            }
            return null;
        }
        return null;
    }



}
