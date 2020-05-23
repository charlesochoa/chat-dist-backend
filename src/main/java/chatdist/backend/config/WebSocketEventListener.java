package chatdist.backend.config;

import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import chatdist.backend.util.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(),
                SimpMessageHeaderAccessor.class);
        GenericMessage<?> generic = (GenericMessage<?>) accessor.getHeader("simpConnectMessage");
        Map<String, String> nativeHeaders = (Map<String, String>) generic.getHeaders().get("nativeHeaders");
        String sessionId = (String) accessor.getMessageHeaders().get("simpSessionId");
        String token = String.valueOf(nativeHeaders.get("Authorization"));
        if (token != "null") {
            // Remove brackets from token
            token = token.substring(1, token.length() - 1);
            final String username = JWTUtils.getUsernameFromToken(token);
            Optional<User> u = userRepository.findByUsername(username);
            if (u.get() != null) {
                logger.info("Adding new session {} with username {}...", sessionId, u.get().getUsername());
                if (sessions.containsValue(u.get().getUsername())) {
                    throw new Exception();
                }
                sessions.put(sessionId, u.get().getUsername());
            }
        }
        logger.info("Received a new web socket connection!");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(),
                SimpMessageHeaderAccessor.class);
        String sessionId = (String) accessor.getMessageHeaders().get("simpSessionId");
        logger.info("Disconnection event received!");
        logger.info("Sessions: {}", sessions);
        logger.info("SessionId: {}", sessionId);
        if (sessionId != null) {
            String toLogout = sessions.get(sessionId);
            logger.info("User {} disconnected from the web socket...", toLogout);
            for (Iterator<User> iterator = queues.iterator(); iterator != null && iterator.hasNext();) {
                User itUser =  iterator.next();
                if (itUser.getUsername().equals(toLogout)) {
                    iterator.remove();
                    break;
                }
            }
            sessions.remove(sessionId);
        }
    }

    @Bean
    public HashMap<String, String> sessions() {
        sessions = new HashMap<String, String>() ;
        return sessions;
    }
}