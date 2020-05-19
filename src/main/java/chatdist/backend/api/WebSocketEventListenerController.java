package chatdist.backend.api;

import chatdist.backend.model.User;
import chatdist.backend.util.RabbitMQConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequestMapping
@EnableScheduling
public class WebSocketEventListenerController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListenerController.class);

    @Autowired
    private Channel channel;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/receive-message")
    public void receive(@Payload User user) throws Exception {
        System.out.println("Preparing reception in queue: " + user.getBindingName());
        channel.queueDeclare(user.getBindingName(), true, false, false, null);
        GetResponse response = channel.basicGet(user.getBindingName(), RabbitMQConstants.AUTO_ACK);
        do {
            if (response == null) {
                Thread.sleep(1000);
            } else {
                byte[] body = response.getBody();
                this.messagingTemplate.convertAndSend("/topic/chat/" + user.getUsername(),
                        (String) new String(body));
            }
            response = channel.basicGet(user.getBindingName(), RabbitMQConstants.AUTO_ACK);
        } while (true);
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        logger.info("Disconnection event received");
        if(username != null) {
            logger.info("User Disconnected : " + username);

//            AuxMessage chatMessage = new AuxMessage();
//            chatMessage.setMsg("Disconnected");
//
//            messagingTemplate.convertAndSend("/topic/chat-send", chatMessage);
        }
    }
}