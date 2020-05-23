package chatdist.backend.config;

import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.annotation.Resource;
import java.util.*;

@Configuration
@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private UserRepository userRepository;

    private HashMap<String, String> sessions;

    @Resource(name = "queues")
    private Set<User> queues;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) throws Exception {

        SimpMessageHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(), SimpMessageHeaderAccessor.class);
        System.out.println(accessor.getMessageHeaders().get("simpSessionId"));

        System.out.println("accessor");
        System.out.println(accessor);
        GenericMessage<?> generic = (GenericMessage<?>) accessor.getHeader("simpConnectMessage");
        System.out.println("generic");
        System.out.println(generic);
        Map<String, String> nativeHeaders = (Map<String, String>) generic.getHeaders().get("nativeHeaders");
        System.out.println("nativeHeaders");
        System.out.println(nativeHeaders);
        String sessionId =(String) accessor.getMessageHeaders().get("simpSessionId");
        String tempS =String.valueOf(nativeHeaders.get("username"));
        final String username = tempS.substring(1,tempS.length()-1);
        Optional<User> u = userRepository.findByUsername(username);
        if(u.get()!=null){
            System.out.println("Agregando Nueva sesión: " + sessionId + ", " + u.get().getUsername());
            if(sessions.containsValue(u.get().getUsername()))
            {
                throw new Exception();
            }
            sessions.put(sessionId,u.get().getUsername());
        }
        System.out.println(sessions.keySet());
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(), SimpMessageHeaderAccessor.class);
        String sessionId =(String) accessor.getMessageHeaders().get("simpSessionId");
        logger.info("Disconnection event received");
        if (sessionId != null) {

            String toLogout = sessions.get(sessionId);
            logger.info("User Disconnected : " + toLogout);

//            remove from queue
            System.out.println("Antes queues");
            System.out.println(queues);
            for (Iterator<User> iterator = queues.iterator(); iterator!=null && iterator.hasNext();) {
                User itUser =  iterator.next();
                if (itUser.getUsername().equals(toLogout)) {
                    iterator.remove();
                    break;
                }

            }
            sessions.remove(sessionId);
            System.out.println("Despues queues");
            System.out.println(queues);

//            AuxMessage chatMessage = new AuxMessage();
//            chatMessage.setMsg("Disconnected");
//
//            messagingTemplate.convertAndSend("/topic/chat-send", chatMessage);
        }
    }

    @Bean
    public HashMap<String, String> sessions() {
        sessions = new HashMap<String, String>() ;
        return sessions;
    }
}