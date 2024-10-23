package com.example.hospital.service;

import com.example.hospital.client.KeycloakClient;
import com.example.hospital.client.RoleKeycloak;
import com.example.hospital.entity.TokenRequest;
import com.example.hospital.entity.Utente;
import com.example.hospital.entity.UtenteKeycloak;
import com.example.hospital.repository.UtenteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakClient keycloakClient;

    @Autowired
    private UtenteRepository utenteRepository;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    public String login(String username, String password, String clientId, String clientSecret) {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "password");
        map.put("username", username);
        map.put("password", password);
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);

        // Richiesta del token
        TokenRequest tokenRequest = new TokenRequest(username, password, clientId, clientSecret, "password");
        ResponseEntity<Object> responseEntity = keycloakClient.getAccessToken(tokenRequest);
        ObjectMapper objectMapper = new ObjectMapper();

        Map mapResponse = objectMapper.convertValue(responseEntity.getBody(), Map.class);
        return mapResponse.get("access_token").toString();
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
        keycloak.setEnabled(true); // Utente attivo
        return keycloak;
    }

    // Uso del metodo login per recuperare il token di accesso per l'admin
    public String getAdminToken() {
        return login(adminUsername, adminPassword, clientId, clientSecret);
    }

    // Creazione dell'utente in Keycloak
    public Utente createUtenteInKeycloak(Utente utente) throws FeignException {
        String accessToken = getAdminToken();
        String authorizationHeader = "Bearer " + accessToken;
        UtenteKeycloak utenteKeycloak = utenteKeycloak(utente);

        // Creazione dell'utente in Keycloak
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

}
