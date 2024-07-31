package ploton.SpringSecurity_JWT.cfg;

import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.Collection;

@Getter
@Slf4j
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String token;

    public JwtAuthenticationToken(UserDetails userDetails, String token, Collection<? extends GrantedAuthority> authorities) {
        super(userDetails, null, authorities);
        this.token = token;
        log.info("JwtAuthenticationToken.\nDetails: " + userDetails + "\nToken: " + token + "\nAuthorities: " + authorities);
    }

}
