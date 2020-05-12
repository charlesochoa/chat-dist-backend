package chatdist.backend.api;

import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Channel channel;

    @PostMapping(path="/sign-up")
    public @ResponseBody User signUp(@RequestBody User user) throws IOException, TimeoutException {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (!optionalUser.isPresent()) {
            User newUser = new User(user.getUsername(), user.getPassword());
            channel.queueDeclare(newUser.getBindingName(),true,false,false,null);
            channel.queueBind(newUser.getBindingName(),RabbitMQConstants.EXCHANGE_NAME,
                    newUser.getBindingName());
            channel.queueBind(newUser.getBindingName(),RabbitMQConstants.EXCHANGE_NAME,
                    RabbitMQConstants.ADMIN_ROUTING_KEY);
            User savedUser = userRepository.save(newUser);
            return savedUser;
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Username already exists"
        );
    }

    @PostMapping(path="/sign-in")
    public @ResponseBody
    Iterable<User> signIn(@RequestBody User user) throws IOException, TimeoutException {
        System.out.println("SIGNING IN");
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        System.out.println(user.getUsername());
        if (optionalUser.isPresent()) {
            System.out.println("User Found!");
            System.out.println(optionalUser.get().getPassword());
            System.out.println(passwordEncoder.encode(user.getPassword()));
            if(optionalUser.get().getPassword().equals( user.getPassword())){

                return userRepository.findAll() ;
            }
            return null;
        }
        return null;
    }
}
