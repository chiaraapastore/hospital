package com.example.hospital.service;

import com.example.hospital.client.KeycloakClient;
import com.example.hospital.entity.TokenRequest;
import com.example.hospital.entity.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakClient keycloakClient;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    protected String clientId;

    @Value("${keycloak.credentials.secret}")
    protected String clientSecret;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    public TokenResponse login(String username, String password) {

        TokenRequest tokenRequest = new TokenRequest(username, password, clientId, clientSecret, "password");


        //TokenResponse tokenResponse = keycloakClient.getAccessToken(tokenRequest);

        //return tokenResponse != null ? tokenResponse : null;
        return keycloakClient.getAccessToken(tokenRequest);

    }

    public String getAccessToken(String username, String password) {
        TokenResponse tokenResponse = login(username, password);
        return tokenResponse != null ? tokenResponse.getAccessToken() : null;
    }
}
