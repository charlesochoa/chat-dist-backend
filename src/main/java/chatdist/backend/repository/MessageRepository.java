package chatdist.backend.repository;

import java.util.List;

import chatdist.backend.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
//    List<Message> findByName(String name);
    Message findById(long id);
}