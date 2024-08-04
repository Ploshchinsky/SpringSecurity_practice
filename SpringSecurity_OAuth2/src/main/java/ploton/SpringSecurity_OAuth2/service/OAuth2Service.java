package ploton.SpringSecurity_OAuth2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Service {
    private final OAuth2AuthorizedClientService oauth2ClientService;

    public OAuth2Service(OAuth2AuthorizedClientService oauth2ClientService) {
        this.oauth2ClientService = oauth2ClientService;
    }

    public String getAccessToken(Authentication authentication) {
        String principleName = authentication.getName();
        OAuth2AuthorizedClient client = oauth2ClientService.loadAuthorizedClient(
                "gitHub", principleName
        );
        if (client != null) {
            OAuth2AccessToken accessToken = client.getAccessToken();
            return accessToken.getTokenValue();
        }
        return null;
    }
}
