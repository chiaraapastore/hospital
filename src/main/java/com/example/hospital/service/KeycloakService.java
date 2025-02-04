package com.example.hospital.service;

import com.example.hospital.client.KeycloakClient;
import com.example.hospital.models.*;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.UtenteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class KeycloakService {


    private final KeycloakClient keycloakClient;
    private final UtenteRepository utenteRepository;
    private final DepartmentRepository departmentRepository;


    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    public String authenticate(String username, String password) {

        TokenRequest tokenRequest = new TokenRequest(username, password, clientId, clientSecret, "password");
        ResponseEntity<Object> responseEntity = keycloakClient.getAccessToken(tokenRequest);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> mapResponse = objectMapper.convertValue(responseEntity.getBody(), Map.class);
            return mapResponse.get("access_token").toString();
        } else {
            throw new RuntimeException("Login failed with status: " + responseEntity.getStatusCode());
        }

    }

    public Utente createUser(Utente utente) throws FeignException {

        String accessToken = authenticate(adminUsername, adminPassword);
        String authorizationHeader = "Bearer " + accessToken;
        UtenteKeycloak utenteKeycloak = utenteKeycloak(utente);
        ResponseEntity<Object> response = keycloakClient.createUsers(authorizationHeader, utenteKeycloak);
        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Errore durante la creazione dell'utente in Keycloak: " +
                    (response != null ? response.getBody() : "Nessuna risposta dal server."));
        }

        String location = response.getHeaders().get("location").get(0);
        String[] locationParts = location.split("/");
        String userId = locationParts[locationParts.length - 1];
        utente.setId(userId);
        addRole(authorizationHeader,userId,"capo-reparto");
        Utente savedUtente = utenteRepository.save(utente);
        return savedUtente;
    }


   private void addRole(String authorizationHeader, String userId, String roleName) throws FeignException{

        ResponseEntity<List<RoleKeycloak>> rolesResponse = keycloakClient.getAvailableRoles(authorizationHeader, userId, "0", "100", "");

        if (rolesResponse == null || !rolesResponse.getStatusCode().is2xxSuccessful() || rolesResponse.getBody() == null) {
            throw new RuntimeException("Errore durante il recupero dei ruoli disponibili da Keycloak");
        }

        RoleKeycloak capoRepartoRole = rolesResponse.getBody().stream()
                .filter(role -> "capo-reparto".equals(role.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ruolo 'capo-reparto' non trovato nei ruoli disponibili"));


        String clientIdRole = capoRepartoRole.getClientId();
        List<RoleRepresentation> rolesToAssign = List.of(
                new RoleRepresentation(capoRepartoRole.getName(), capoRepartoRole.getDescription(), false)
        );

        ResponseEntity<Object> roleResponse = keycloakClient.addRoleToUser(authorizationHeader, userId, clientIdRole, rolesToAssign);

        if (roleResponse == null || !roleResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Errore durante l'assegnazione del ruolo su Keycloak: " +
                    (roleResponse != null ? roleResponse.getBody() : "Nessuna risposta dal server."));
        }
   }


   private UtenteKeycloak utenteKeycloak(Utente utente) {
        UtenteKeycloak keycloak = new UtenteKeycloak();
        keycloak.setUsername(utente.getUsername());
        keycloak.setFirstName(utente.getFirstName());
        keycloak.setLastName(utente.getLastName());
        keycloak.setEmail(utente.getEmail());

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(utente.getPassword());
        credentialRepresentation.setTemporary(false);

        keycloak.setCredentials(List.of(credentialRepresentation));
        keycloak.setEnabled(true);
        return keycloak;
    }


}

