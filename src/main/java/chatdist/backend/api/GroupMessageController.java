package chatdist.backend.api;

import chatdist.backend.model.GroupMessage;
import chatdist.backend.repository.GroupMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
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
}