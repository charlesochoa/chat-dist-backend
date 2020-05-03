package chatdist.backend.repository;

import chatdist.backend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByName(String name);

    User findByEmail(String email);
}
