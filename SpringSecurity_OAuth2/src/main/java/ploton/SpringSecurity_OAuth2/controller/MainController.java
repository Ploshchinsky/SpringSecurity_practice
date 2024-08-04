package ploton.SpringSecurity_OAuth2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ploton.SpringSecurity_OAuth2.service.OAuth2Service;
import ploton.SpringSecurity_OAuth2.integration.Client;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final Client client;
    private final OAuth2Service oAuth2Service;

    @GetMapping("/home")
    public ResponseEntity<?> home(Authentication authentication) {
        return ResponseEntity.ok("Home Content");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        log.warn("User LOGIN: ID - " + user.getAttribute("id") + ", Name - " + user.getAttribute("name"));

        String accessToken = oAuth2Service.getAccessToken(authentication);
        String email = client.getEmail(accessToken);

        return ResponseEntity.ok("Profile Content:" +
                "\nName - " + user.getAttribute("name") +
                "\nEmail - " + email +
                "\nRole - " + user.getAttribute("type") +
                "\nauthenticated - " + authentication.isAuthenticated()
        );

    }

    @GetMapping("/admin")
    public ResponseEntity<?> admin(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        return ResponseEntity.ok("Admin Content ACCESS COMPLETE:"
                + " Name - " + user.getAttribute("name")
                + ", Role - " + user.getAttribute("type"));

    }

    @GetMapping("/user")
    public ResponseEntity<?> user(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        return ResponseEntity.ok("User Content ACCESS COMPLETE: Name - " + user.getAttribute("name")
                + ", Role - " + user.getAttribute("type"));

    }

    @GetMapping("/delete")
    public ResponseEntity<?> delete(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        log.warn("User DELETE token: ID - " + user.getAttribute("id"));

        String accessToken = oAuth2Service.getAccessToken(authentication);
        client.deleteToken(accessToken);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/revoke")
    public ResponseEntity<?> revoke(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        log.warn("User REVOKE token: ID - " + user.getAttribute("id"));

        String accessToken = oAuth2Service.getAccessToken(authentication);
        return client.revokeToken(accessToken);
    }
}
