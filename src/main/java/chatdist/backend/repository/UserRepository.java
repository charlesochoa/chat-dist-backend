package chatdist.backend.repository;

import chatdist.backend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByName(String name);
    User findById(long id);
    User findByEmail(String email);
}
