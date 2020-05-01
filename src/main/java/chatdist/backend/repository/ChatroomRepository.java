package chatdist.backend.repository;

import java.util.List;
import java.util.Optional;

import chatdist.backend.model.Chatroom;
import chatdist.backend.model.User;
import org.springframework.data.repository.CrudRepository;

public interface ChatroomRepository extends CrudRepository<Chatroom, Long> {
    List<Chatroom> findByName(String name);

    Optional<Chatroom> findById(Long id);

    List<Chatroom> findByAdmin(User admin);
}
