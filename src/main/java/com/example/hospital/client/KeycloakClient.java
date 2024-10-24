package com.example.hospital.client;

import com.example.hospital.entity.TokenRequest;
import org.springframework.util.MultiValueMap;
import com.example.hospital.entity.UtenteKeycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "keycloakClient", url = "${keycloak.auth-server-url}")
public interface KeycloakClient {

        @RequestMapping(method = RequestMethod.POST,
                value = "/realms/${keycloak.realm}/protocol/openid-connect/token",
                consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ResponseEntity<Object> getAccessToken(TokenRequest tokenRequest);

        @RequestMapping(method = RequestMethod.POST,
                value = "/admin/realms/${keycloak.realm}/users",
                produces = "application/json")
        ResponseEntity<Object> createUsers(@RequestHeader("Authorization") String accessToken, @RequestBody UtenteKeycloak utenteKeycloak);

        @RequestMapping(method = RequestMethod.GET,
                value = "/admin/realms/${keycloak.realm}/ui-ext/available-roles/users/{id}",
                produces = "application/json")
        ResponseEntity<List<RoleKeycloak>> getAvailableRoles(@RequestHeader("Authorization") String accessToken, @PathVariable("id") String id, @RequestParam(value = "first", defaultValue = "0") String first, @RequestParam(value = "max", defaultValue = "100") String max, @RequestParam(value = "search", defaultValue = "") String search);

        @RequestMapping(method = RequestMethod.POST,
                value = "/admin/realms/${keycloak.realm}/users/{id}/role-mappings/clients/{clientIdRole}",
                produces = "application/json")
        ResponseEntity<Object> addRoleToUser(@RequestHeader("Authorization") String accessToken, @PathVariable String id, @PathVariable String clientIdRole, @RequestBody List<RoleRepresentation> roles);

        @PostMapping("/realms/{realm}/protocol/openid-connect/token")
        ResponseEntity<Map<String, Object>> login(
                @RequestParam("username") String username,
                @RequestParam("password") String password,
                @RequestParam("client_id") String clientId,
                @RequestParam("client_secret") String clientSecret
        );

}


