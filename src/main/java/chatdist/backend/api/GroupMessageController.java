package chatdist.backend.api;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.DirectMessage;
import chatdist.backend.model.GroupMessage;
import chatdist.backend.model.User;
import chatdist.backend.repository.ChatroomRepository;
import chatdist.backend.repository.GroupMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(path="/group-message")
public class GroupMessageController {
    @Autowired
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private ChatroomRepository chatroomRepository;

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