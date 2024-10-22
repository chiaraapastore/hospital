package com.example.hospital.service;

import com.example.hospital.client.KeycloakClient;
import com.example.hospital.client.RoleKeycloak;
import com.example.hospital.entity.TokenRequest;
import com.example.hospital.entity.Utente;
import com.example.hospital.entity.UtenteKeycloak;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakClient keycloakClient;

    private RoleKeycloak roleKeycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    protected String clientId;

    @Value("${keycloak.credentials.secret}")
    protected String clientSecret;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    public String login(String username, String password, String clientId, String clientSecret) {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "password");
        map.put("username", username);
        map.put("password", password);
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        TokenRequest tokenRequest = new TokenRequest(username, password, clientId, clientSecret, "password");
        ResponseEntity<Object> responseEntity = keycloakClient.getAccessToken(tokenRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        Map mapResponse = objectMapper.convertValue(responseEntity.getBody(), Map.class);
        return mapResponse.get("access_token").toString();
    }

    //Metodo crea utentekeycloak
    private UtenteKeycloak utenteKeycloak(Utente utente) {
        UtenteKeycloak keycloak = new UtenteKeycloak();
        keycloak.setUsername(utente.getUsername());
        keycloak.setFirstName(utente.getFirstName());
        keycloak.setLastName(utente.getLastName());
        keycloak.setEmail(utente.getEmail());
        //Password
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(utente.getPassword());
        credentialRepresentation.setTemporary(false);
        //Set credenziali
        keycloak.setCredentials(List.of(credentialRepresentation));
        keycloak.setEnabled(true);//attivo
        return keycloak;
    }

    // Uso il metodo login per recuperare il token di accesso per l'admin
    public String getAdminToken() {
        return login("admin", "admin", clientId, clientSecret);
    }

    public void createUtenteInKeycloak(Utente utente) {
       //Token di accesso per l'admin
        String accessToken = getAdminToken();
        // Converto l'oggetto Utente in UtenteKeycloak
        UtenteKeycloak utenteKeycloak = utenteKeycloak(utente);
        // Creo l'utente in Keycloak
        ResponseEntity<Object> response = keycloakClient.createUsers("Bearer " + accessToken, utenteKeycloak);
        // Controllo se la creazione dell'utente è avvenuta con successo
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Errore nella creazione dell'utente in Keycloak: " + response.getBody());
        }
        // Assegno i ruoli all'utente
        for (String role : utenteKeycloak.getRealmRoles()) {
            // Recupero i ruoli disponibili per l'utente
            ResponseEntity<List<RoleKeycloak>> availableRolesResponse = keycloakClient.getAvailableRoles(
                    "Bearer " + accessToken, clientId, "0", "100", role
            );

            // Verifico se la richiesta dei ruoli disponibili è andata a buon fine
            if (availableRolesResponse.getStatusCode().is2xxSuccessful()) {
                List<RoleKeycloak> roles = availableRolesResponse.getBody();
                // Trovo e assegna il ruolo specificato all'utente
                for (RoleKeycloak availableRole : roles) {
                    List<RoleKeycloak> clientRoles = roles.stream().filter(roleClient -> roleClient.getRole().equals(utente.getRole())).collect(Collectors.toList());
                    if (availableRole.getId().equals(role)) {
                        keycloakClient.addRoleToUser(accessToken, utente.getId(), clientId , clientRoles.stream().map(RoleKeycloak::toRoleRepresentation).collect(Collectors.toList()));
                        break;
                    }
                }
            } else {
                throw new RuntimeException("Errore nel recupero dei ruoli disponibili: " + availableRolesResponse.getBody());
            }
        }
    }


}
