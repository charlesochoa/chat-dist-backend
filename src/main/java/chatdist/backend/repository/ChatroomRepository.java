package chatdist.backend.repository;

import java.util.List;

import chatdist.backend.model.Chatroom;
import org.springframework.data.repository.CrudRepository;

public interface ChatroomRepository extends CrudRepository<Chatroom, Long> {
    List<Chatroom> findByName(String name);
    Chatroom findById(long id);
}
