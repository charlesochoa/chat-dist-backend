package chatdist.backend.api;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.User;
import chatdist.backend.repository.ChatroomRepository;
import chatdist.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
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

    @PostMapping(path="/add-user")
    public @ResponseBody Chatroom addUserChatroom(@RequestParam Long id, User user) {
        Optional<Chatroom> c = chatroomRepository.findById(id);
        if (c.isPresent()) {
            c.get().addUser(user);
            chatroomRepository.save(c.get());
            return c.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
        );
    }
}