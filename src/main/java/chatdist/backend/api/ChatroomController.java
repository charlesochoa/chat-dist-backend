package chatdist.backend.api;

import chatdist.backend.model.Chatroom;
import chatdist.backend.repository.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/chatroom")
public class ChatroomController {
    @Autowired
    private ChatroomRepository chatroomRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewChatroom(@RequestParam String name) {
        Chatroom c = new Chatroom(name);
        chatroomRepository.save(c);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Chatroom> getAllChatrooms() {
        return chatroomRepository.findAll();
    }
}