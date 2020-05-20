package chatdist.backend.api;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.GroupMessage;
import chatdist.backend.repository.ChatroomRepository;
import chatdist.backend.repository.GroupMessageRepository;
import chatdist.backend.util.RabbitMQConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(path="/group-messages")
public class GroupMessageController {
    @Autowired
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private Channel channel;

    @PostMapping("/send-group-message/{chatroomId}")
    public @ResponseBody GroupMessage sendGroupMessage(@RequestBody GroupMessage message,
                                                       @PathVariable Long chatroomId)
            throws IOException, TimeoutException {
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(chatroomId);
        if (optionalChatroom.isPresent()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonStr = objectMapper.writeValueAsString(message);
                message.setChatroom(optionalChatroom.get());
                channel.basicPublish(RabbitMQConstants.EXCHANGE_NAME, message.getChatRoom().getBindingName(),
                        null, jsonStr.getBytes());
                groupMessageRepository.save(message);
                return message;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
        );
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<GroupMessage> getAllGroupMessages() {
        return groupMessageRepository.findAll();
    }

    @GetMapping(path="/all/{chatroomId}")
    public @ResponseBody Iterable<GroupMessage> getAllGroupMessagesByChatroomId(@PathVariable Long chatroomId) {
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(chatroomId);
        if (optionalChatroom.isPresent()) {
            return groupMessageRepository.getGroupMessagesByChatroom(optionalChatroom.get());
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chatroom not found"
        );
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteGroupMessage(@PathVariable Long id) {
        if (groupMessageRepository.existsById(id)) {
            groupMessageRepository.deleteById(id);
            return ResponseEntity.noContent().header("Content-Length", "0").build();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Group message not found"
        );
    }
}