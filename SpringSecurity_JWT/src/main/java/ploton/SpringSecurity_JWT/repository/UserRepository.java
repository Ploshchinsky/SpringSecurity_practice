package ploton.SpringSecurity_JWT.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ploton.SpringSecurity_JWT.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
