package chatdist.backend.api;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    @PutMapping(path="/{id}")
    public @ResponseBody User updateUser(@PathVariable Long id, @RequestParam String name) {
        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()) {
            u.get().setName(name);
            userRepository.save(u.get());
            return u.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        );
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().header("Content-Length", "0").build();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        );
    }
}