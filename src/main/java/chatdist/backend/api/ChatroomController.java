package chatdist.backend.api;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.User;
import chatdist.backend.repository.ChatroomRepository;
import chatdist.backend.repository.UserRepository;
import chatdist.backend.util.RabbitMQConstants;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(path="/chatroom")
public class ChatroomController {
    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Channel channel;

    @PostMapping(path="/add")
    public @ResponseBody Chatroom addNewChatroom(@RequestBody Chatroom chatroom,
                                                 @RequestParam Long userId) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User admin = optionalUser.get();
            chatroom.addUser(admin);
            chatroomRepository.save(chatroom);
            return chatroom;
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        );
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Chatroom> getAllChatrooms() {
        return chatroomRepository.findAll();
    }

    @GetMapping(path="/all/{userId}")
    public @ResponseBody Iterable<Chatroom> getAllChatroomsByParticipant(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return chatroomRepository.findByParticipant(optionalUser.get());
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        );
    }

    @PostMapping(path="/{id}/add-user/{userId}")
    public @ResponseBody Chatroom addUserToChatroom(@PathVariable Long id,
                                                    @PathVariable Long userId) throws IOException {
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(id);
        if (optionalChatroom.isPresent()) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                optionalChatroom.get().addUser(optionalUser.get());
                channel.queueBind(optionalUser.get().getBindingName(), RabbitMQConstants.EXCHANGE_NAME,
                        optionalChatroom.get().getBindingName());
                chatroomRepository.save(optionalChatroom.get());
                return optionalChatroom.get();
            }
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found"
            );
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
        );
    }

    @PutMapping(path="/{id}")
    public @ResponseBody Chatroom updateChatroom(@PathVariable Long id, @RequestParam String name) {
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(id);
        if (optionalChatroom.isPresent()) {
            optionalChatroom.get().setName(name);
            chatroomRepository.save(optionalChatroom.get());
            return optionalChatroom.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
        );
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteChatroom(@PathVariable Long id) {
        if (chatroomRepository.existsById(id)) {
            chatroomRepository.deleteById(id);
            return ResponseEntity.noContent().header("Content-Length", "0").build();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
        );
    }
}