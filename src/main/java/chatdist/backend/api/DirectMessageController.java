package chatdist.backend.api;

import chatdist.backend.model.DirectMessage;
import chatdist.backend.repository.DirectMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/direct-message")
public class DirectMessageController {
    @Autowired
    private DirectMessageRepository directMessageRepository;

//    @PostMapping(path="/add")
//    public @ResponseBody String addNewMessage(@RequestParam String name
//            , @RequestParam String email) {
//        DirectMessage m = new DirectMessage(name, email);
//        directMessageRepository.save(m);
//        return "Saved";
//    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<DirectMessage> getAllDirectMessages() {
        return directMessageRepository.findAll();
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