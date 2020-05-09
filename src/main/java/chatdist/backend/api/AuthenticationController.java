package chatdist.backend.api;

import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/auth")
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path="/sign-up")
    public @ResponseBody User signUp(@RequestBody User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (!optionalUser.isPresent()) {
            User newUser = new User(user.getUsername(), user.getEmail(), passwordEncoder.encode(user.getPassword()));
            userRepository.save(newUser);
            return newUser;
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Username already exists"
        );
    }

    @PostMapping(path="/sign-in")
    public @ResponseBody User signIn(@RequestBody User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            User u = optionalUser.get();
            Boolean matches = passwordEncoder.matches(user.getPassword(), u.getPassword());
            if (matches) {
                return u;
            }
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Invalid credentials for username or password"
        );
    }
}
