package chatdist.backend.api;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.User;
import chatdist.backend.repository.ChatroomRepository;
import chatdist.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/chatroom")
public class ChatroomController {
    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public @ResponseBody Chatroom addNewChatroom(@RequestParam String name, @RequestParam Long id) {
        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()) {
            User admin = u.get();
            Chatroom c = new Chatroom(name, admin);
            chatroomRepository.save(c);
            c.setBindingName(c.getName().concat(c.getId().toString()));
            return c;
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        );
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Chatroom> getAllChatrooms() {
        return chatroomRepository.findAll();
    }

    @PostMapping(path="/{id}/add-user/{userId}")
    public @ResponseBody Chatroom addUserChatroom(@PathVariable Long id, @PathVariable Long userId) {
        Optional<Chatroom> c = chatroomRepository.findById(id);
        if (c.isPresent()) {
            Optional<User> u = userRepository.findById(userId);
            if (u.isPresent()) {
                c.get().addUser(u.get());
                chatroomRepository.save(c.get());
                return c.get();
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
        Optional<Chatroom> c = chatroomRepository.findById(id);
        if (c.isPresent()) {
            c.get().setName(name);
            chatroomRepository.save(c.get());
            return c.get();
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