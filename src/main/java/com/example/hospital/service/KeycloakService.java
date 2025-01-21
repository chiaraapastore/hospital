package com.example.hospital.service;

import com.example.hospital.client.KeycloakClient;
import com.example.hospital.client.RoleKeycloak;
import com.example.hospital.models.*;
import com.example.hospital.repository.UtenteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakService {


    @Autowired
    private final KeycloakClient keycloakClient;
    private final UtenteRepository utenteRepository;


    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.auth-server-url}")
    private String urlKeycloak;


    @Value("${keycloak.admin.client-id}")
    private String clientIdAdmin;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecretAdmin;

    @Value("${keycloak.admin.access}")
    private String clientId;

    @Value("${keycloak.admin.access.secret}")
    private String clientSecret;

    public String login(String username, String password) {

        String clientId = this.clientId;
        TokenRequest tokenRequest = new TokenRequest(username, password, clientId, clientSecret, "password");
        ResponseEntity<Object> responseEntity = keycloakClient.getAccessToken(tokenRequest);

        // Log della risposta
        System.out.println("Risposta da Keycloak: " + responseEntity);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> mapResponse = objectMapper.convertValue(responseEntity.getBody(), Map.class);
            return mapResponse.get("access_token").toString();
        } else {
            // Stampa il corpo della risposta per aiutare nel debug
            System.err.println("Errore nella risposta: " + responseEntity.getBody());
            throw new RuntimeException("Login failed with status: " + responseEntity.getStatusCode());
        }

    }

    public Utente createUtenteInKeycloak(Utente utente) throws FeignException {
        String accessToken = getAdminAccessToken();
        String authorizationHeader = "Bearer " + accessToken;
        UtenteKeycloak utenteKeycloak = utenteKeycloak(utente);
        ResponseEntity<Object> response = keycloakClient.createUsers(authorizationHeader, utenteKeycloak);
        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Errore durante la creazione dell'utente in Keycloak: " +
                    (response != null ? response.getBody() : "Nessuna risposta dal server."));
        }
        // Estrazione dell'ID dell'utente creato
        String location = response.getHeaders().get("location").get(0);
        String[] locationParts = location.split("/");
        String userId = locationParts[locationParts.length - 1];
        utente.setId(userId);
        Utente savedUtente = utenteRepository.save(utente);
        // Assegnazione dei ruoli all'utente
        assignRolesToUser(authorizationHeader, userId, utente.getRole());
        return savedUtente;
    }


    private void assignRolesToUser(String authorizationHeader, String userId, String role) {
        String clientId = "hospital-app";
        try {
            ResponseEntity<List<RoleKeycloak>> rolesResponse = keycloakClient.getAvailableRoles(
                    authorizationHeader, userId, "0", "100", clientId);

            List<RoleKeycloak> roleList = rolesResponse.getBody();

            // Stampo i dettagli dei ruoli recuperati
            System.out.println("Ruoli disponibili:");
            roleList.forEach(r -> {
                System.out.println("Ruolo: " + r.getRole() + ", Client ID: " + r.getClientId());
            });

            // Verifico se il ruolo esiste nella lista dei ruoli
            Optional<RoleKeycloak> selectedRoleOpt = roleList.stream()
                    .filter(r -> r.getRole().equals(role))
                    .findFirst();

            if (!selectedRoleOpt.isPresent()) {
                throw new RuntimeException("Ruolo non trovato: " + role + ". Ruoli disponibili: " +
                        roleList.stream().map(RoleKeycloak::getRole).collect(Collectors.joining(", ")));
            }

            RoleKeycloak selectedRole = selectedRoleOpt.get();
            String clientIdRole = Optional.ofNullable(selectedRole.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client ID non trovato per il ruolo: " + selectedRole.getRole()));

            // Creo la lista di ruoli da assegnare
            List<RoleRepresentation> rolesToAssign = List.of(selectedRole.toRoleRepresentation());

            // Assegno il ruolo all'utente
            ResponseEntity<Object> addRoleResponse = keycloakClient.addRoleToUser(
                    authorizationHeader, userId, clientIdRole, rolesToAssign);

            // Controllo la risposta dell'assegnazione
            if (addRoleResponse == null || !addRoleResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Errore durante l'assegnazione del ruolo: " +
                        (addRoleResponse != null ? addRoleResponse.getBody() : "Nessuna risposta dal server."));
            }

            System.out.println("Ruolo assegnato con successo all'utente con ID: " + userId);

        } catch (FeignException e) {
            throw new RuntimeException("Errore durante l'assegnazione dei ruoli: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Errore generale durante l'assegnazione dei ruoli: " + e.getMessage(), e);
        }
    }


    public List<LoginRequest> filterLoginRequestsByClientId(List<LoginRequest> loginRequests, String clientId) {
        return loginRequests.stream()
                .filter(loginRequest -> loginRequest.getClientId().equals(clientId))
                .collect(Collectors.toList());
    }

    /*
    public List<String> filterTokensByClientId(List<TokenResponse> tokens, String clientId) {
        return tokens.stream()
                .filter(token -> tokenResponse.getClientId().equals(clientId))
                .map(TokenResponse::getAccessToken)
                .collect(Collectors.toList());
    }
*/
    private List<LoginRequest> getAllLoginRequests() {
        return new ArrayList<>();
    }

    private List<TokenResponse> getAllTokens() {
        return new ArrayList<>();
    }

    // Metodo per creare UtenteKeycloak
    private UtenteKeycloak utenteKeycloak(Utente utente) {
        UtenteKeycloak keycloak = new UtenteKeycloak();
        keycloak.setUsername(utente.getUsername());
        keycloak.setFirstName(utente.getFirstName());
        keycloak.setLastName(utente.getLastName());
        keycloak.setEmail(utente.getEmail());

        // Impostazione della password
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(utente.getPassword());
        credentialRepresentation.setTemporary(false);

        // Set credenziali
        keycloak.setCredentials(List.of(credentialRepresentation));
        keycloak.setEnabled(false); // Impostato a false per scelta dell'amministratore
        return keycloak;
    }

    public String getAdminAccessToken() {
        return login(adminUsername,adminPassword);
    }

}

