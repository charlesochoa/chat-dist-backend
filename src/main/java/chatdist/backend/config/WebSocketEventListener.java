package chatdist.backend.config;

import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import chatdist.backend.util.RabbitMQConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Resource(name = "queues")
    private Set<User> queues;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) throws IOException, InterruptedException {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(), SimpMessageHeaderAccessor.class);
        accessor.getMessageHeaders();
        GenericMessage<?> generic = (GenericMessage<?>) accessor.getHeader("simpConnectMessage");
        System.out.println(generic);
        Map<String, String> nativeHeaders = (Map<String, String>) generic.getHeaders().get("nativeHeaders");
        String username1 =String.valueOf(nativeHeaders.get("username"));
        final String username = username1.substring(1,username1.length()-1);
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        System.out.println(headerAccessor.getSessionAttributes());
//        System.out.println(headerAccessor);
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
        logger.info("Disconnection event received");
        if (username != null) {
            logger.info("User Disconnected : " + username);

//            remove from queue
            System.out.println("Antes queues");
            System.out.println(queues);
            queues = queues.stream()
                    .filter(s -> s.getUsername() != username) // keep the odds
                    .collect(Collectors.toSet());
            System.out.println("Despues queues");
            System.out.println(queues);

//            AuxMessage chatMessage = new AuxMessage();
//            chatMessage.setMsg("Disconnected");
//
//            messagingTemplate.convertAndSend("/topic/chat-send", chatMessage);
        }
    }
}