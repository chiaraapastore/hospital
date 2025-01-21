package com.example.hospital.controller;

import com.example.hospital.models.TokenRequest;
import com.example.hospital.models.Utente;
import com.example.hospital.service.KeycloakService;
import com.example.hospital.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UtenteController {

    private final UtenteService utenteService;
    private final KeycloakService keycloakService;

    @Autowired
    public UtenteController(UtenteService utenteService, KeycloakService keycloakService) {
        this.utenteService = utenteService;
        this.keycloakService = keycloakService;
    }

    @GetMapping("/utenti")
    public List<Utente> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

    @GetMapping("/utenti/{email}")
    public ResponseEntity<Utente> getUtenteByEmail(@PathVariable String email) {
        Utente utente = utenteService.getUtenteByEmail(email);
        if (utente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(utente);
    }

    @PostMapping("/create/users/keycloak")
    public ResponseEntity<Object> createUtenteKeycloak(@RequestBody Utente utente) {
        try {
            Utente savedUtente = keycloakService.createUtenteInKeycloak(utente);
            return ResponseEntity.ok(savedUtente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/utenti/{id}")
    public ResponseEntity<Utente> updateUtente(@PathVariable String id, @RequestBody Utente utenteDetails) {
        Utente updatedUtente = utenteService.updateUtente(id, utenteDetails);
        if (updatedUtente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUtente);
    }

    @DeleteMapping("/utenti/{email}")
    public ResponseEntity<Void> deleteUtente(@PathVariable String email) {
        boolean isDeleted = utenteService.deleteUtente(email);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody TokenRequest tokenRequest) {
        String token = keycloakService.login(tokenRequest.getUsername(), tokenRequest.getPassword());
        return ResponseEntity.ok(token);
    }

}


