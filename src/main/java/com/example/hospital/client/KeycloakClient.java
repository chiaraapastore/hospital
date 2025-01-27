package com.example.hospital.client;

import com.example.hospital.models.RoleKeycloak;
import com.example.hospital.models.TokenRequest;
import com.example.hospital.models.UtenteKeycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "keycloakClient", url = "${keycloak.admin.auth-server-url}")
public interface KeycloakClient {

        @RequestMapping(method = RequestMethod.POST,
                value = "/realms/${keycloak.admin.realm}/protocol/openid-connect/token",
                consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ResponseEntity<Object> getAccessToken(@RequestBody TokenRequest tokenRequest);

        @RequestMapping(method = RequestMethod.POST,
                value = "/admin/realms/${keycloak.admin.realm}/users",
                produces = "application/json")
        ResponseEntity<Object> createUsers(@RequestHeader("Authorization") String accessToken, @RequestBody UtenteKeycloak utenteKeycloak);

        @RequestMapping(method = RequestMethod.GET,
                value = "/admin/realms/${keycloak.admin.realm}/ui-ext/available-roles/users/{id}",
                produces = "application/json")
        ResponseEntity<List<RoleKeycloak>> getAvailableRoles(@RequestHeader("Authorization") String accessToken, @PathVariable("id") String id, @RequestParam(value = "first", defaultValue = "0") String first, @RequestParam(value = "max", defaultValue = "100") String max, @RequestParam(value = "search", defaultValue = "") String search);


        @RequestMapping(method = RequestMethod.POST,
                value = "/admin/realms/${keycloak.admin.realm}/users/{id}/role-mappings/clients/{clientIdRole}",
                produces = "application/json")
        ResponseEntity<Object> addRoleToUser(
                @RequestHeader("Authorization") String accessToken,
                @PathVariable String id,
                @PathVariable String clientIdRole,
                @RequestBody List<RoleRepresentation> roles);

}


