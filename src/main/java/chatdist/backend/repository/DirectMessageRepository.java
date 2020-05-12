package chatdist.backend.repository;

import chatdist.backend.model.DirectMessage;
import chatdist.backend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectMessageRepository extends BaseMessageRepository<DirectMessage> {
    @Query("SELECT d FROM DirectMessage d WHERE d.receiver = ?1 OR d.sender = ?1")
    List<DirectMessage> getDirectMessagesByUser(User user);
}
