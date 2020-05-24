package chatdist.backend.repository;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.GroupMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GroupMessageRepository extends BaseMessageRepository<GroupMessage> {
    @Query("SELECT g FROM GroupMessage g WHERE g.chatroom = ?1")
    List<GroupMessage> getGroupMessagesByChatroom(Chatroom chatroom);

    List<GroupMessage> getGroupMessagesByText(Boolean text);

    @Query("SELECT min(g.time) FROM GroupMessage g WHERE g.text = ?1")
    Timestamp getMinTimestamp(Boolean text);

    @Query("SELECT max(g.time) FROM GroupMessage g WHERE g.text = ?1")
    Timestamp getMaxTimestamp(Boolean text);

    @Query("SELECT count(g) FROM GroupMessage g WHERE g.text = ?1")
    int getTotalGroupMessages(Boolean text);

    @Query("SELECT sum(g.bytes) FROM GroupMessage g WHERE g.text = ?1")
    Long getTotalBytes(Boolean text);
}
