package chatdist.backend.repository;

import chatdist.backend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE NOT 'ADMIN' IN (SELECT r.value FROM u.roles r)")
    List<User> findAllNormalUsers();
}
