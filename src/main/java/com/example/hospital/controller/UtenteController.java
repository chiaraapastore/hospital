package com.example.hospital.controller;

import com.example.hospital.client.KeycloakClient;
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
    private KeycloakClient keycloakClient;


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
            keycloakService.createUtenteInKeycloak(utente);
            return ResponseEntity.status(HttpStatus.CREATED).body("Utente creato con successo");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nella creazione dell'utente");
        }
    }

    @PostMapping("/create/users/mongo")
    public ResponseEntity<Object> createUtenteMongo(@RequestBody Utente utente) {
        try {
            utenteService.createUtente(utente);  // Salva l'utente
            return ResponseEntity.status(HttpStatus.CREATED).body("Utente creato con successo usando MongoDB");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nella creazione dell'utente con MongoDB");
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
    public String login(@RequestBody TokenRequest loginRequest) {
        return keycloakService.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getClient_id(), loginRequest.getClient_secret());

    }

}


