package chatdist.backend.api;

import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public @ResponseBody User addNewUser(@RequestParam String name
            , @RequestParam String email) {
        User u = new User(name, email);
        userRepository.save(u);
        return u;
    }

    @PostMapping(path="/signup")
    public @ResponseBody Iterable<User> signUp(@RequestParam String name
            , @RequestParam String email) {
        User user = userRepository.findByEmail(email);
        Iterable<User> users =  userRepository.findAll();
        if (user == null) {
            User u = new User(name, email);
            userRepository.save(u);
            return users;
        } else {
            return users;
        }
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}