package ploton.SpringSecurity_JWT.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ploton.SpringSecurity_JWT.dto.JwtRequest;
import ploton.SpringSecurity_JWT.dto.JwtResponse;
import ploton.SpringSecurity_JWT.entity.User;
import ploton.SpringSecurity_JWT.exception.Error;
import ploton.SpringSecurity_JWT.jwt.JwtUtil;
import ploton.SpringSecurity_JWT.service.UserBlockingService;
import ploton.SpringSecurity_JWT.service.UserService;

import java.util.Date;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final UserBlockingService userBlockingService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        String username = authRequest.getUsername();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        System.out.println("Failed Attempts: " + user.getFailedAttempts());
        if (userBlockingService.isLocked(user)) {
            System.out.println("USER IS BLOCKED");
            log.warn("!!! User {} is locked due to too many failed login attempts", username);
            throw new LockedException("User " + username + " is locked");
        }

        try {
            log.info("User login -> {}", authRequest.getUsername());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            log.info("User authentication complete -> {}", authRequest.getUsername());

            UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
            String token = JwtUtil.generateToken(userDetails);
            userBlockingService.resetFailedAttempts(user);

            return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);

        } catch (BadCredentialsException ex) {
            log.warn("BadCredentialsException. Incorrect username or password -> " + authRequest.getUsername()
                    + "\nReason: " + ex.getMessage());

            userBlockingService.handleFailedAttempts(user);

            Error error = new Error("BadCredentialsException", "Incorrect username or password",
                    HttpStatus.UNAUTHORIZED.value(), new Date());

            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }
}
