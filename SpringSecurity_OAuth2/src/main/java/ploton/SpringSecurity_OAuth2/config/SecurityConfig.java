package ploton.SpringSecurity_OAuth2.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import ploton.SpringSecurity_OAuth2.entity.Role;
import ploton.SpringSecurity_OAuth2.entity.User;
import ploton.SpringSecurity_OAuth2.integration.Client;
import ploton.SpringSecurity_OAuth2.service.OAuth2Service;
import ploton.SpringSecurity_OAuth2.service.RoleService;
import ploton.SpringSecurity_OAuth2.service.UserService;

import java.security.SecureRandom;
import java.util.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final UserService userService;
    private final RoleService roleService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/user").hasRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login
                        .defaultSuccessUrl("/profile"))
                .build();
    }

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    @Bean
    GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            authorities.forEach(authority -> {
                if (authority instanceof OAuth2UserAuthority oauth2UserAuthority) {
                    Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
                    String username = (String) userAttributes.get("name");

                    String roleToNewUser = ((OAuth2UserAuthority) authority).getAuthority().contains("USER") ?
                            "ROLE_USER" : "ROLE_AUTHENTICATED";
                    User user = userService.findByUsername(
                            username).orElseGet(() -> createUser(username, roleToNewUser));
                    grantedAuthorities.addAll(user.getRoles().
                            stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .toList());
                }
            });
            return grantedAuthorities;
        };
    }

    private User createUser(String username, String roleName) {
        String password = generatePassword();
        User newUser = new User();
        newUser.setId(userService.size());
        newUser.setUsername(username);
        newUser.setPassword(password);

        Role role = roleService.findByName(roleName)
                .orElseThrow(() -> new NoSuchElementException(roleName + " not found"));

        newUser.setRoles(List.of(role));

        log.warn("New User has been created. User: " + newUser);
        return userService.save(newUser);
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16]; // Размер может быть изменен в зависимости от требований
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
