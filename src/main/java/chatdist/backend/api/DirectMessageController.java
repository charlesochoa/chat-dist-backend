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
    public @ResponseBody DirectMessage sendMessage(@RequestBody DirectMessage message)
            throws IOException, TimeoutException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonStr = objectMapper.writeValueAsString(message);
            channel.basicPublish(RabbitMQConstants.EXCHANGE_NAME, message.getReceiver().getBindingName(),
                    null, jsonStr.getBytes());
            DirectMessage newDirectMessage = directMessageRepository.save(message);
            return newDirectMessage;
        } catch (IOException e) {
            e.printStackTrace();
            throw  e;
        }
    }

    @PostMapping("/upload-file")
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

//    @GetMapping("/downloadFile/{fileName:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
//        // Load file as Resource
//        Resource resource = (Resource) fileStorageService.loadFileAsResource(fileName);
//
//        // Try to determine file's content type
//        String contentType = null;
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException ex) {
//            System.out.println("Could not determine file type.");
//        }
//
//        // Fallback to the default content type if type could not be determined
//        if(contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }
}