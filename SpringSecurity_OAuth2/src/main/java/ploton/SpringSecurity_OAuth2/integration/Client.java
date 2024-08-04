package ploton.SpringSecurity_OAuth2.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ploton.SpringSecurity_OAuth2.dto.EmailDto;

import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class Client {
    @Value("${githubApiUrl}")
    private String githubApiUrl;
    @Value("${spring.security.oauth2.client.registration.gitHub.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.gitHub.client-secret}")
    private String clientSecret;
    private final RestTemplate restTemplate;

    public String getEmail(String accessToken) {
        String url = githubApiUrl + "user/emails";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return extractEmailFromResponseBody(response.getBody());
    }

    public void deleteToken(String accessToken) {
        String url = githubApiUrl + "applications/" + clientId + "/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.set("Accept", "application/vnd.github+json");

        Map<String, Object> params = new HashMap<>();
        params.put("access_token", accessToken);

        String requestBody = "{\"access_token\":\"" + accessToken + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            log.warn("Token DELETE successfully.");
        } else {
            log.warn("Failed to DELETE token. Status code: " + response.getStatusCode());
        }
    }

    public ResponseEntity<?> revokeToken(String accessToken) {
        String url = githubApiUrl + "applications/" + clientId + "/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.set("Accept", "application/vnd.github+json");

        Map<String, Object> params = new HashMap<>();
        params.put("access_token", accessToken);

        String requestBody = "{\"access_token\":\"" + accessToken + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.warn("Token REVOKE successfully.");
            return response;
        } else {
            log.warn("Failed to REVOKE token. Status code: " + response.getStatusCode());
        }
        return ResponseEntity.badRequest().body("Failed to REVOKE token. Status code");
    }


    private String extractEmailFromResponseBody(String responseBody) {
        try {
            EmailDto[] userEmails = new ObjectMapper().readValue(responseBody, EmailDto[].class);
            return userEmails[0].getEmail();
        } catch (JsonProcessingException e) {
            log.warn("JsonProcessingException. " + e.getMessage());
            return null;
        }
    }
}
