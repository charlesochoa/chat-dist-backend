package chatdist.backend.api;

import chatdist.backend.model.DirectMessage;
import chatdist.backend.model.User;
import chatdist.backend.repository.DirectMessageRepository;
import chatdist.backend.repository.UserRepository;
import chatdist.backend.service.FileStorageService;
import chatdist.backend.util.RabbitMQConstants;
import chatdist.backend.util.UploadFileResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(path="/direct-messages")
public class DirectMessageController {
    @Autowired
    private DirectMessageRepository directMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Channel channel;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/send-direct-message")
    public @ResponseBody DirectMessage sendMessage(@RequestBody DirectMessage message) throws IOException, TimeoutException {
        System.out.println("Trying send direct message!");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonStr = objectMapper.writeValueAsString(message);
            channel.basicPublish(RabbitMQConstants.EXCHANGE_NAME, message.getReceiver().getBindingName(),
                    null, jsonStr.getBytes());
            DirectMessage newM = directMessageRepository.save(message);
            return newM;
        } catch (IOException e) {
            e.printStackTrace();
            throw  e;
        }
    }

    @PostMapping("/send-file")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<DirectMessage> getAllDirectMessages() {
        return directMessageRepository.findAll();
    }

    @GetMapping(path="/all/{userId}")
    public @ResponseBody Iterable<DirectMessage> getAllDirectMessagesByUser(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return directMessageRepository.getDirectMessagesByUser(optionalUser.get());
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        );
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteDirectMessage(@PathVariable Long id) {
        if (directMessageRepository.existsById(id)) {
            directMessageRepository.deleteById(id);
            return ResponseEntity.noContent().header("Content-Length", "0").build();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Direct message not found"
        );
    }
}