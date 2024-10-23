package com.example.hospital.controller;

import com.example.hospital.entity.TokenRequest;
import com.example.hospital.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.hospital.entity.Utente;
import com.example.hospital.service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;
    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    public UtenteController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }


    @GetMapping
    public List<Utente> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

    @GetMapping("/{email}")
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

    @PutMapping("/{id}")
    public ResponseEntity<Utente> updateUtente(@PathVariable String id, @RequestBody Utente utenteDetails) {
        Utente updatedUtente = utenteService.updateUtente(id, utenteDetails);
        if (updatedUtente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUtente);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUtente(@PathVariable String email) {
        boolean isDeleted = utenteService.deleteUtente(email);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody TokenRequest loginRequest) {
        try {
            String token = keycloakService.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getClient_id(), loginRequest.getClient_secret());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }


}


