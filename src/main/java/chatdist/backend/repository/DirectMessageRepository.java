package chatdist.backend.repository;

import chatdist.backend.model.DirectMessage;
import chatdist.backend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface DirectMessageRepository extends BaseMessageRepository<DirectMessage> {
    @Query("SELECT d FROM DirectMessage d WHERE d.receiver = ?1 OR d.sender = ?1")
    List<DirectMessage> getDirectMessagesByUser(User user);

    List<DirectMessage> getDirectMessagesByText(Boolean text);

    @Query("SELECT min(d.time) FROM DirectMessage d WHERE d.text = ?1 AND NOT d.sender IS NULL")
    Timestamp getMinTimestamp(Boolean text);

    @Query("SELECT max(d.time) FROM DirectMessage d WHERE d.text = ?1 AND NOT d.sender IS NULL")
    Timestamp getMaxTimestamp(Boolean text);

    @Query("SELECT count(d) FROM DirectMessage d WHERE d.text = ?1 AND NOT d.sender IS NULL")
    Integer getTotalDirectMessages(Boolean text);

    @Query("SELECT count(d) FROM DirectMessage d WHERE d.time >= ?1")
    Integer getTotalMessagesFromTime(Timestamp t);

    @Query("SELECT sum(d.bytes) FROM DirectMessage d WHERE d.time >= ?1")
    Integer getTotalBytesFromTime(Timestamp t);

    @Query("SELECT sum(d.bytes) FROM DirectMessage d WHERE d.text = ?1 AND NOT d.sender IS NULL")
    Long getTotalBytes(Boolean text);
}
