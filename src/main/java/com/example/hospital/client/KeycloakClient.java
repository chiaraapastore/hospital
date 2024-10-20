package com.example.hospital.client;

import com.example.hospital.entity.TokenRequest;
import com.example.hospital.entity.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "KeycloakClient", url = "${keycloak.auth-server-url}")
public interface KeycloakClient {

        @RequestMapping(method = RequestMethod.POST,
                value = "/realms/${keycloak.realm}/protocol/openid-connect/token",
                consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        TokenResponse getAccessToken(@RequestBody TokenRequest tokenRequest);
}


