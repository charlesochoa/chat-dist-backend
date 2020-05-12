package chatdist.backend.api;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.User;
import chatdist.backend.repository.ChatroomRepository;
import chatdist.backend.repository.UserRepository;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(path="/chatroom")
public class ChatroomController {
    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private UserRepository userRepository;

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

    public ChatroomController() throws IOException, TimeoutException {
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

    @PostMapping(path="/add")
    public @ResponseBody Chatroom addNewChatroom(@RequestParam String name, @RequestParam Long id) throws IOException {
        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()) {
            User admin = u.get();
            Chatroom c = new Chatroom(name, admin);
            c.addUser(admin);
            chatroomRepository.save(c);
            return c;
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        );
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Chatroom> getAllChatrooms() {
        return chatroomRepository.findAll();
    }

    @GetMapping(path="/all/{userId}")
    public @ResponseBody Iterable<Chatroom> getAllChatroomsByParticipant(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return chatroomRepository.findByParticipant(optionalUser.get());
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        );
    }

    @PostMapping(path="/{id}/add-user/{userId}")
    public @ResponseBody Chatroom addUserChatroom(@PathVariable Long id, @PathVariable Long userId) throws IOException {
        Optional<Chatroom> c = chatroomRepository.findById(id);
        if (c.isPresent()) {
            Optional<User> u = userRepository.findById(userId);
            if (u.isPresent()) {
                c.get().addUser(u.get());
                channel.queueBind(u.get().getBindingName(),EXCHANGE_NAME,c.get().getBindingName());
                chatroomRepository.save(c.get());
                return c.get();
            }
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found"
            );
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
        );
    }

    @PutMapping(path="/{id}")
    public @ResponseBody Chatroom updateChatroom(@PathVariable Long id, @RequestParam String name) {
        Optional<Chatroom> c = chatroomRepository.findById(id);
        if (c.isPresent()) {
            c.get().setName(name);
            chatroomRepository.save(c.get());
            return c.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
        );
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteChatroom(@PathVariable Long id) {
        if (chatroomRepository.existsById(id)) {
            chatroomRepository.deleteById(id);
            return ResponseEntity.noContent().header("Content-Length", "0").build();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
        );
    }
}