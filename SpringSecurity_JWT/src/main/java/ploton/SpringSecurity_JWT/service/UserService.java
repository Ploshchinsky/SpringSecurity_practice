package ploton.SpringSecurity_JWT.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploton.SpringSecurity_JWT.entity.User;
import ploton.SpringSecurity_JWT.repository.RoleRepository;
import ploton.SpringSecurity_JWT.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)
        ));
        log.info("User details loaded -> username: {}", username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
    }

    public User createNewUser(User user) {
        if (userRepository.existsById(user.getId())) {
            log.error("User creation failed -> User ID {} already exists", user.getId());
            throw new IllegalArgumentException("User ID - " + user.getId() + " already exist");
        }
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
        log.info("New user created -> username: {}", user.getUsername());
        return userRepository.save(user);
    }
}
