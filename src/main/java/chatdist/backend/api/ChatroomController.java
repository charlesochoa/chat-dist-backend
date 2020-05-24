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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path="/chatrooms")
public class ChatroomController {
    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Channel channel;

    @PostMapping(path="/add")
    public @ResponseBody Chatroom addNewChatroom(@RequestBody Chatroom chatroom) throws IOException {
        Optional<User> optionalUser = userRepository.findById(chatroom.getAdmin().getId());
        if (optionalUser.isPresent()) {
            User admin = optionalUser.get();
            Chatroom newChatroom = new Chatroom(chatroom.getName(), admin);
            newChatroom.addUser(admin);
            chatroomRepository.save(newChatroom);
            newChatroom.setBindingName("chatroom." + newChatroom.getId().toString() + "." + admin.getUsername());
            chatroomRepository.save(newChatroom);
            channel.queueBind(admin.getBindingName(), RabbitMQConstants.EXCHANGE_NAME,
                    newChatroom.getBindingName());
            return newChatroom;
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

    @GetMapping(path="/{id}/users")
    public @ResponseBody Iterable<User> getAllUsersFromChatroom(@PathVariable Long id) {
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(id);
        if (optionalChatroom.isPresent()) {
            Set<User> users = optionalChatroom.get().getUsers();
            return users;
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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