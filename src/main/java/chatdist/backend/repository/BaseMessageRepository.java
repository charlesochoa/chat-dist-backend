package chatdist.backend.repository;

import chatdist.backend.model.BaseMessage;
import chatdist.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseMessageRepository<E extends BaseMessage> extends CrudRepository<E, Long> {
    Optional<E> findById(Long id);

    List<BaseMessage> findByTime(Timestamp time);

    List<BaseMessage> findBySender(User sender);
}