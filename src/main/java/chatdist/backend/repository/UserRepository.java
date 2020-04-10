package chatdist.backend.repository;

import java.util.List;

import chatdist.backend.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByName(String name);
    User findById(long id);
}
