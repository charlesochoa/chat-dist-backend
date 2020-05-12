package chatdist.backend.api;

import chatdist.backend.model.User;
import chatdist.backend.util.RabbitMQConstants;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableScheduling
public class AuxiliarController {
    @Autowired
    private Channel channel;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/chat-receive")
    public void receive(@Payload User user) throws Exception {
        System.out.println("Preparing reception in queue: " + user.getBindingName());
        channel.queueDeclare(user.getBindingName(), true, false, false, null);
        GetResponse response = channel.basicGet(user.getBindingName(), RabbitMQConstants.AUTO_ACK);
        do {
            if (response == null) {
                Thread.sleep(1000);
            } else {
                byte[] body = response.getBody();
                this.template.convertAndSend("/topic/chat/" + user.getUsername(),(String) new String(body));
            }
            response = channel.basicGet(user.getBindingName(), RabbitMQConstants.AUTO_ACK);
        } while (true);
    }
}
