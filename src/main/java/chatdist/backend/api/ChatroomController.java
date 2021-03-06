package chatdist.backend.api;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.CustomUserDetails;
import chatdist.backend.model.GroupMessage;
import chatdist.backend.model.User;
import chatdist.backend.repository.ChatroomRepository;
import chatdist.backend.repository.GroupMessageRepository;
import chatdist.backend.repository.UserRepository;
import chatdist.backend.util.RabbitMQConstants;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private GroupMessageRepository groupMessageRepository;

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
                try {
                    optionalChatroom.get().addUser(optionalUser.get());
                    channel.queueBind(optionalUser.get().getBindingName(), RabbitMQConstants.EXCHANGE_NAME,
                            optionalChatroom.get().getBindingName());
                    chatroomRepository.save(optionalChatroom.get());

                } catch (Exception e)
                {
                    throw new ResponseStatusException(
                            HttpStatus.FOUND, "User already added to chatroom"
                    );
                }
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

    @PostMapping(path="/{id}/remove-user/{userId}")
    public @ResponseBody Chatroom removeUserFromChatroom(@PathVariable Long id,
                                                    @PathVariable Long userId) throws IOException {
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(id);

        if (optionalChatroom.isPresent()) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                if (optionalChatroom.get().removeUser(optionalUser.get())) {
                    channel.queueUnbind(optionalUser.get().getBindingName(), RabbitMQConstants.EXCHANGE_NAME,
                            optionalChatroom.get().getBindingName());
                    chatroomRepository.save(optionalChatroom.get());
                    return optionalChatroom.get();
                }
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found in the chatroom"
                );
            }
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found"
            );
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
        );
    }

    @GetMapping(path="/{id}")
    public @ResponseBody Chatroom getChatroom(@PathVariable Long id) {
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(id);
        if (optionalChatroom.isPresent()) {
            return optionalChatroom.get();
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
    public ResponseEntity<Void> deleteChatroom(@PathVariable Long id) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.toString();
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(id);
        if (optionalChatroom.isPresent()) {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isPresent()) {
                if (optionalUser.get() == optionalChatroom.get().getAdmin()) {
                    Set<User> userSet = optionalChatroom.get().getUsers();
                    for (User u: userSet) {
                        channel.queueUnbind(u.getBindingName(), RabbitMQConstants.EXCHANGE_NAME,
                                optionalChatroom.get().getBindingName());
                    }
                    optionalChatroom.get().clearUsers();
                    for (GroupMessage g : optionalChatroom.get().getGroupMessages()) {
                        groupMessageRepository.deleteById(g.getId());
                    }
                    optionalChatroom.get().clearGroupMessages();
                    chatroomRepository.save(optionalChatroom.get());
                    chatroomRepository.deleteById(id);
                    return ResponseEntity.noContent().header("Content-Length", "0").build();
                }
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User is not the admin"
                );
            }
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Admin not found"
            );
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
        );
    }
}