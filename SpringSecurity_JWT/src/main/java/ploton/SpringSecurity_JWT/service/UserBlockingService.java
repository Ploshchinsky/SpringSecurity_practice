package ploton.SpringSecurity_JWT.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ploton.SpringSecurity_JWT.entity.User;
import ploton.SpringSecurity_JWT.repository.UserRepository;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserBlockingService {
    private final UserRepository userRepository;
    private static final Integer MAX_FAILED_ATTEMPTS = 3;
    private static final Long LOCK_LIFETIME = 36000L;

    public void handleFailedAttempts(User user) {
        int attempts = user.getFailedAttempts();
        if (attempts + 1 >= MAX_FAILED_ATTEMPTS) {
            user.setBlockingTime(new Date(System.currentTimeMillis()));
        }
        user.setFailedAttempts(attempts + 1);
        userRepository.save(user);
    }

    public boolean isLocked(User user) {
        if (user.getBlockingTime() == null) {
            return false;
        }
        Date endLock = new Date(user.getBlockingTime().getTime() + LOCK_LIFETIME);
        return new Date(System.currentTimeMillis()).before(endLock);
    }

    public void resetFailedAttempts(User user) {
        user.setFailedAttempts(0);
        user.setBlockingTime(null);
        userRepository.save(user);
    }
}
