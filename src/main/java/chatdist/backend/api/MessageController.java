package chatdist.backend.api;

import chatdist.backend.model.AuxMessage;
import chatdist.backend.model.Message;
import chatdist.backend.model.User;
import chatdist.backend.repository.MessageRepository;
import chatdist.backend.repository.UserRepository;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping(path="/message")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    private static boolean autoAck = true;
    private static String userName = "byfntbvj";
    private static String password = "2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE";
    private static String vHost = "byfntbvj";
    private static String hostName = "kangaroo.rmq.cloudamqp.com";
    private static int portNumber = 5672;
    private static String uri = "amqp://byfntbvj:2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE@kangaroo.rmq.cloudamqp.com/byfntbvj";
    private ConnectionFactory factory;

//    @PostMapping(path="/add")
//    public @ResponseBody String addNewMessage(@RequestParam String name
//            , @RequestParam String email) {
//        Message m = new Message(name, email);
//        messageRepository.save(m);
//        return "Saved";
//    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @PostMapping(path="/send")
    public @ResponseBody String sendMessage(@RequestParam Long s, @RequestParam Long r
            , @RequestParam String c) throws IOException, TimeoutException {
        User sender = null;
        User receiver = null;
        Iterable<User> users =  userRepository.findAll();
        for (User u: users
             ) {
            System.out.println("Searching user");
            if (u.getId().equals(s)){
                System.out.println("Found sender");
                sender = new User(u.getName(),u.getEmail());
                sender.setQueueName(u.getQueueName());
                sender.setId(u.getId());
            } else if (u.getId().equals(r)){
                System.out.println("Found receiver");
                receiver = new User(u.getName(),u.getEmail());
                receiver.setQueueName(u.getQueueName());
                receiver.setId(u.getId());
            }
        }
        if (sender != null && receiver != null){

            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();
            channel.queueDeclare(receiver.getQueueName(), false, false, false, null);
            String message = sender.getName() + ": " + c;
            channel.basicPublish("", receiver.getQueueName(), null, message.getBytes());
            channel.close();
            conn.close();
            return "Success";
        }
            return "Error, user not found";
    }



}