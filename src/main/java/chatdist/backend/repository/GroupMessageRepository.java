package chatdist.backend.repository;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.DirectMessage;
import chatdist.backend.model.GroupMessage;
import chatdist.backend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMessageRepository  extends BaseMessageRepository<GroupMessage> {
    @Query("SELECT g FROM GroupMessage g WHERE g.chatroom = ?1")
    List<GroupMessage> getGroupMessagesByChatroom(Chatroom chatroom);
}
