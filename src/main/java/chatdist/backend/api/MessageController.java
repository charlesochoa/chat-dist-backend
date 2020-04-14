package chatdist.backend.api;

import chatdist.backend.model.Message;
import chatdist.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/message")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

//    @PostMapping(path="/add")
//    public @ResponseBody String addNewMessage(@RequestParam String name
//            , @RequestParam String email) {
//        Message m = new Message(name, email);
//        messageRepository.save(m);
//        return "Saved";
//    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}