package com.example.hospital.service;

import com.example.hospital.client.KeycloakClient;
import com.example.hospital.entity.TokenRequest;
import com.example.hospital.repository.UtenteRepository;
import com.nimbusds.oauth2.sdk.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    @Autowired
    private final KeycloakClient keycloakClient;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    protected String clientId;

    @Value("${keycloak.credentials.secret}")
    protected String clientSecret;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;


    // Effettua il login e restituisce il token di accesso
    public ResponseEntity<TokenResponse> login(String username, String password) {
        TokenRequest tokenRequest = new TokenRequest(username, password, clientId, clientSecret, "password");

        ResponseEntity<TokenResponse> response = keycloakClient.getAccessToken(tokenRequest);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return keycloakClient.getAccessToken(tokenRequest);
        }
        return null; // O gestire il caso di errore come preferisci
    }

    @Autowired
    @Qualifier("authorizedClientManager")
    private OAuth2AuthorizedClientManager utenti;

    // Ottiene il token di accesso dall'utente già autenticato
    public String getAccessToken(Object principal) {
        try {
            ClientRegistration clientRegistration = UtenteRepository.findByRegistrationId(clientId);
            OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(clientRegistration.getRegistrationId())
                    .principal(String.valueOf(principal))
                    .build();
            OAuth2AuthorizedClient client = utenti.authorize(oAuth2AuthorizeRequest);
            if (client != null) {
                return client.getAccessToken().getTokenValue();
            }
        } catch (Exception exp) {
            // Gestione degli errori
        }
        return null; // O gestire il caso di errore come preferisci
    }

}

