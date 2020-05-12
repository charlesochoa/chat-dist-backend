package chatdist.backend.repository;

import java.util.List;
import java.util.Optional;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ChatroomRepository extends CrudRepository<Chatroom, Long> {
    Optional<Chatroom> findByName(String name);

    Optional<Chatroom> findByAdmin(User admin);

    @Query("SELECT c FROM Chatroom c WHERE ?1 in (SELECT u FROM c.users u)")
    List<Chatroom> findByParticipant(User user);
}
