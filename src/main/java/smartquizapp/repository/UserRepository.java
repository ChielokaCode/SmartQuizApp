package smartquizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import smartquizapp.model.User;

import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<UserDetails> findByEmail(String email);
    boolean existsByEmail(String email);

    User findUserByEmail(String email);
}