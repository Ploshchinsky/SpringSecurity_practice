package ploton.SpringSecurity_JWT.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class JwtUtil {
    private static final String SECRET_KEY = "666ajjsu999";
    private static final Long LIFETIME = 3600000l;


    public static String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + LIFETIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = extractClaims(token);
        String userName = claims.getSubject();
        return (userName.equals(userDetails.getUsername())
                &&
                !isTokenExpired(token));
    }

    private static boolean isTokenExpired(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().before(new Date());
    }

    private static Claims extractClaims(String token) {
        return (Claims) Jwts.parser().setSigningKey(SECRET_KEY).parse(token).getBody();
    }

    public static String getUserName(String token) {
        return extractClaims(token).getSubject();
    }

    public static List<String> getRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }
}
