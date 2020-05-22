package chatdist.backend.api;

import chatdist.backend.model.DirectMessage;
import chatdist.backend.model.User;
import chatdist.backend.util.RabbitMQConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Set;

@Controller
public class WebSocketController {
    @Autowired
    private Channel channel;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Resource(name = "queues")
    private Set<User> queues;

    @MessageMapping("/receive-message")
    public void receive(@Payload User user) throws Exception {
        System.out.println("Preparing reception in queue: " + user.getBindingName());
        queues.add(user);
        System.out.println("Elements in QUEUE: " + queues);
        if(queues.size()<=1){

            do {
                for (User loggedIn: queues) {
                    GetResponse response = channel.basicGet(loggedIn.getBindingName(), RabbitMQConstants.AUTO_ACK);
                    if (response == null) {
                        Thread.sleep(100);
                    } else {
                        byte[] body = response.getBody();
                        this.messagingTemplate.convertAndSend("/topic/chat/" + loggedIn.getUsername(),
                                (String) new String(body));
                    }
                }
            } while (queues.size() > 0);
            System.out.println("Receive going to close! ");
        } else {
            System.out.println("Queue larger than 1");
        }
    }
}
