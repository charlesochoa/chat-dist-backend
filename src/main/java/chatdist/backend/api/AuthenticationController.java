package chatdist.backend.api;

import chatdist.backend.model.Role;
import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import chatdist.backend.repository.RoleRepository;
import chatdist.backend.util.RabbitMQConstants;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(path="/auth")
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Channel channel;

    @PostMapping(path="/sign-up")
    public @ResponseBody User signUp(@RequestBody User user) throws IOException, TimeoutException {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (!optionalUser.isPresent()) {
            User newUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()));
            Optional<Role> optionalRole = roleRepository.findByValue("NORMAL");
            if (optionalRole.isPresent()) {
                channel.queueDeclare(newUser.getBindingName(), true, false, false, null);
                channel.queueBind(newUser.getBindingName(), RabbitMQConstants.EXCHANGE_NAME,
                        newUser.getBindingName());
                channel.queueBind(newUser.getBindingName(), RabbitMQConstants.EXCHANGE_NAME,
                        RabbitMQConstants.ADMIN_ROUTING_KEY);
                newUser.addRole(optionalRole.get());
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

    @PostMapping(path="/sign-in")
    public @ResponseBody User signIn(@RequestBody User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), optionalUser.get().getPassword())) {
                return optionalUser.get();
            }
            return null;
        }
        return null;
    }
}
