package chatdist.backend.api;

import chatdist.backend.model.Role;
import chatdist.backend.model.User;
import chatdist.backend.repository.RoleRepository;
import chatdist.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(path="/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path="/add")
    public @ResponseBody User addNewUser(@RequestBody User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (!optionalUser.isPresent()) {
            User newUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()));
            Optional<Role> optionalRole = roleRepository.findByValue("NORMAL");
            if (optionalRole.isPresent()) {
                User savedUser = userRepository.save(newUser);
                return savedUser;
            }
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Role 'NORMAL' not found"
            );
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Username already exists"
        );
    }

    @GetMapping(path="/{username}")
    public @ResponseBody User getUserByUsername(@PathVariable String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        );
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path="/all/normal")
    public @ResponseBody Iterable<User> getAllNormalUsers() {
        return userRepository.findAllNormalUsers();
    }

    @PutMapping(path="/{id}")
    public @ResponseBody User updateUser(@PathVariable Long id, @RequestParam String email) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            optionalUser.get().setEmail(email);
            userRepository.save(optionalUser.get());
            return optionalUser.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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