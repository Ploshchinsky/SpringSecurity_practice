package ploton.SpringSecurity_OAuth2.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ploton.SpringSecurity_OAuth2.OAuth2Service;

import java.security.Principal;

@RestController
@Slf4j
public class MainController {

    @GetMapping("/home")
    public ResponseEntity<?> home(Authentication authentication) {
        return ResponseEntity.ok("Home Content");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        log.warn("User LOGIN: ID - " + user.getAttribute("id") + ", Name - " + user.getAttribute("name"));
        return ResponseEntity.ok("Profile Content:" +
                "\nName - " + user.getAttribute("name") +
                "\nEmail - " + user.getAttribute("email") +
                "\nRole - " + user.getAttribute("type") +
                "\nauthenticated - " + authentication.isAuthenticated()
        );

    }

    @GetMapping("/admin")
    public ResponseEntity<?> admin(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        if (OAuth2Service.access(OAuth2Service.RoleType.ADMIN, authentication)) {
            return ResponseEntity.ok("Admin Content ACCESS COMPLETE:"
                    + " Name - " + user.getAttribute("name")
                    + ", Role - " + user.getAttribute("type"));
        } else {
            return new ResponseEntity<>("Admin Content ACCESS DENIED: Name - " + user.getAttribute("name")
                    + ", Role - " + user.getAttribute("type"),
                    HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> user(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        if (OAuth2Service.access(OAuth2Service.RoleType.USER, authentication)) {
            return ResponseEntity.ok("User Content ACCESS COMPLETE: Name - " + user.getAttribute("name")
                    + ", Role - " + user.getAttribute("type"));
        }
        return new ResponseEntity<>("User Content ACCESS DENIED: Name - " + user.getAttribute("name")
                + ", Role - " + user.getAttribute("type"),
                HttpStatus.FORBIDDEN);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication, OAuth2AuthenticationToken token) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        System.out.println(token.getCredentials());
        log.warn("User LOGOUT: ID - " + user.getAttribute("id"));
        return ResponseEntity.ok("Logout Complete: ID - " + user.getAttribute("id")
                + ", Authenticated - " + authentication.isAuthenticated());
    }
}
