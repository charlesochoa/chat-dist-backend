package chatdist.backend.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;



@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost("kangaroo.rmq.cloudamqp.com")
                .setRelayPort(61613)
                .setClientLogin("byfntbvj")
                .setClientPasscode("2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE");
    }
}