package chatdist.backend.repository;

import chatdist.backend.model.BaseMessage;
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

    @Query("SELECT min(g.time) FROM GroupMessage g WHERE g.contentType = ?1")
    Timestamp getMinTimestamp(BaseMessage.ContentType contentType);

    @Query("SELECT max(g.time) FROM GroupMessage g WHERE g.contentType = ?1")
    Timestamp getMaxTimestamp(BaseMessage.ContentType contentType);

    @Query("SELECT count(g) FROM GroupMessage g WHERE g.contentType = ?1")
    Integer getTotalGroupMessages(BaseMessage.ContentType contentType);

    @Query("SELECT count(g) FROM GroupMessage g WHERE g.time >= ?1")
    Integer getTotalMessagesFromTime(Timestamp t);

    @Query("SELECT sum(g.bytes) FROM GroupMessage g WHERE g.time >= ?1")
    Integer getTotalBytesFromTime(Timestamp t);

    @Query("SELECT sum(g.bytes) FROM GroupMessage g WHERE g.contentType = ?1")
    Long getTotalBytes(BaseMessage.ContentType contentType);
}
