package chatdist.backend.api;

import chatdist.backend.model.GroupMessage;
import chatdist.backend.repository.GroupMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/group-message")
public class GroupMessageController {
    @Autowired
    private GroupMessageRepository groupMessageRepository;

//    @PostMapping(path="/add")
//    public @ResponseBody String addNewMessage(@RequestParam String name
//            , @RequestParam String email) {
//        DirectMessage m = new DirectMessage(name, email);
//        directMessageRepository.save(m);
//        return "Saved";
//    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<GroupMessage> getAllGroupMessages() {
        return groupMessageRepository.findAll();
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