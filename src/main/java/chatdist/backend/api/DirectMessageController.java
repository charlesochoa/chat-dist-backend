package chatdist.backend.api;

import chatdist.backend.model.DirectMessage;
import chatdist.backend.model.User;
import chatdist.backend.repository.DirectMessageRepository;
import chatdist.backend.repository.UserRepository;
import chatdist.backend.util.RabbitMQConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(path="/direct-messages")
public class DirectMessageController {
    @Autowired
    private DirectMessageRepository directMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Channel channel;

    @PostMapping("/send")
    public @ResponseBody DirectMessage sendMessage(@RequestBody DirectMessage message)
            throws Exception {
        if (message.getContent().length() > 500){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Message too long");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonStr = objectMapper.writeValueAsString(message);
            channel.basicPublish(RabbitMQConstants.EXCHANGE_NAME, message.getReceiver().getBindingName(),
                    null, jsonStr.getBytes());
            DirectMessage newDirectMessage = directMessageRepository.save(message);
            return newDirectMessage;
        } catch (IOException e) {
            e.printStackTrace();
            throw  e;
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/send-all")
    public @ResponseBody DirectMessage sendMessageToAll(@RequestBody DirectMessage message)
            throws IOException, TimeoutException {
        if (message.getContent().length() > 500){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Message too long");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DirectMessage newDirectMessage = directMessageRepository.save(message);
            String jsonStr = objectMapper.writeValueAsString(message);
            channel.basicPublish(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.ADMIN_ROUTING_KEY,
                    null, jsonStr.getBytes());
            return newDirectMessage;
        } catch (IOException e) {
            e.printStackTrace();
            throw  e;
        }
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<DirectMessage> getAllDirectMessages() {
        return directMessageRepository.findAll();
    }

    @GetMapping(path="/all/{userId}")
    public @ResponseBody Iterable<DirectMessage> getAllDirectMessagesByUser(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return directMessageRepository.getDirectMessagesByUser(optionalUser.get());
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        );
    }

    @GetMapping(path="/{id}")
    public @ResponseBody DirectMessage getDirectMessage(@PathVariable Long id) {
        Optional<DirectMessage> optionalDirectMessage = directMessageRepository.findById(id);
        if (optionalDirectMessage.isPresent()) {
            return optionalDirectMessage.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Direct message not found"
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteDirectMessage(@PathVariable Long id) {
        if (directMessageRepository.existsById(id)) {
            directMessageRepository.deleteById(id);
            return ResponseEntity.noContent().header("Content-Length", "0").build();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Direct message not found"
        );
    }
}