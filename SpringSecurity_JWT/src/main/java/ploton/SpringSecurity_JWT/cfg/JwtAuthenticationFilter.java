package ploton.SpringSecurity_JWT.cfg;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ploton.SpringSecurity_JWT.jwt.JwtUtil;
import ploton.SpringSecurity_JWT.service.UserService;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractTokenFromRequest(request);

        if (token != null && validateToken(token)) {
            Authentication authentication = createAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("JwtAuthenticationFilter. Authentication is not complete. SecurityContext not updated");
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.split("Bearer ")[1];
        }
        return null;
    }

    private Authentication createAuthentication(String token) {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider();
        UserDetails userDetails = extractUserDetailsFromToken(token);
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        return provider.authenticate(jwtAuthenticationToken);
    }


    private boolean validateToken(String token) {
        try {
            String username = JwtUtil.getUserName(token);
            UserDetails userDetails = extractUserDetailsFromToken(token);
            if (username != null && userDetails != null) {
                return JwtUtil.validateToken(token, userDetails);
            }
        } catch (Exception ex) {
            log.warn("JwtAuthenticationFilter. ValidateToken Error: " + ex.getMessage());
        }
        return false;
    }

    private UserDetails extractUserDetailsFromToken(String token) {
        try {
            String username = JwtUtil.getUserName(token);
            if (username != null) {
                return userService.loadUserByUsername(username);
            }
        } catch (Exception ex) {
            log.warn("JwtAuthenticationFilter. ExtractUserDetailsFromToken Error: " + ex.getMessage());
        }
        return null;
    }
}
