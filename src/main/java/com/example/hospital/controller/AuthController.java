package com.example.hospital.controller;

import com.example.hospital.entity.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestParam String username, @RequestParam String password) {
        // Eseguo la logica di autenticazione qui utilizzando le credenziali
        // Restituisco il token come risposta
        String token = obtainAccessToken(username, password);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    private String obtainAccessToken(String username, String password) {
        String token = "";
        return token;
    }
}