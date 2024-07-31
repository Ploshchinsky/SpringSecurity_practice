package ploton.SpringSecurity_JWT.cfg;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ploton.SpringSecurity_JWT.jwt.JwtUtil;
import ploton.SpringSecurity_JWT.service.UserService;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (token != null && JwtUtil.validateToken(token, userDetails)) {
            return jwtAuthenticationToken;
        } else {
            throw new BadCredentialsException("Invalid Token");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
