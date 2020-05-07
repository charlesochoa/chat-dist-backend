package chatdist.backend.api;

import chatdist.backend.config.WebSecurityConfig;
import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import chatdist.backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/auth")
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    public @ResponseBody User signIn(@RequestParam String username, @RequestParam String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Boolean matches = passwordEncoder.matches(password, user.getPassword());
            if (matches) {
                return user;
            }
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Invalid credentials for username or password"
        );
    }
}
