package ploton.SpringSecurity_JWT.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MainController {
    @GetMapping("/unsecured")
    public String unsecuredData() {
        return "Unsecured Data";
    }

    @GetMapping("/secured")
    public String securedData() {
        return "Moderator Data";
    }

    @GetMapping("/admin")
    public String adminData() {
        return "Admin Data";
    }

    @GetMapping("/info")
    public String userData(Principal principal) {
        return principal.getName();
    }
}
