package ploton.SpringSecurity_OAuth2.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ploton.SpringSecurity_OAuth2.entity.User;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> findByUsername(String username);

    User save(User user);

    int size();
}
