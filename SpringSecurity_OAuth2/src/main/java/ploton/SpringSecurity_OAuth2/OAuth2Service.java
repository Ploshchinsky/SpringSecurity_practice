package ploton.SpringSecurity_OAuth2;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2Service {
    public static boolean access(RoleType role, Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String authRole = authentication.getAuthorities().toString();
        String userType = user.getAttribute("type");

        return authRole.toLowerCase().contains(role.toString().toLowerCase())
                && userType.toLowerCase().contains(role.toString().toLowerCase());
    }

    public static enum RoleType {
        USER, ADMIN;
    }
}
