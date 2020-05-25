package chatdist.backend.api;

import chatdist.backend.config.WebSocketEventListener;
import chatdist.backend.model.User;
import chatdist.backend.util.RabbitMQConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ConcurrentModificationException;
import java.util.Set;

@Controller
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private Channel channel;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Resource(name = "queues")
    private Set<User> queues;

    @MessageMapping("/receive-message")
    public void receive(@Payload User user) throws Exception {
        logger.info("Preparing reception in queue: {}", user.getBindingName());
        queues.add(user);
        logger.info("Elements in queue: {}", queues);
        if (queues.size() <= 1) {
            do {
                try {
                    for (User loggedIn: queues) {
                        GetResponse response = channel.basicGet(loggedIn.getBindingName(), RabbitMQConstants.AUTO_ACK);
                        if (response == null) {
                            Thread.sleep(100);
                        } else {
                            byte[] body = response.getBody();
                            this.messagingTemplate.convertAndSend("/topic/chat/" + loggedIn.getUsername(),
                                    new String(body));
                        }
                    }
                } catch (ConcurrentModificationException e) {
                    continue;
                }
            } while (queues.size() > 0);
            logger.info("Empty queue, ending receive...");
        } else {
            logger.info("Queue has more than one element!");
        }
    }
}
