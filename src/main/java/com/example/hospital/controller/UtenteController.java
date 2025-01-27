package com.example.hospital.controller;

import com.example.hospital.config.AuthenticationService;
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
@RequestMapping("/api/utente")
public class UtenteController {

    private final UtenteService utenteService;
    private final KeycloakService keycloakService;
    private final AuthenticationService authenticationService;

    public UtenteController(UtenteService utenteService, KeycloakService keycloakService, AuthenticationService authenticationService) {
        this.utenteService = utenteService;
        this.keycloakService = keycloakService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody TokenRequest tokenRequest) {
        try {
            String token = keycloakService.authenticate(tokenRequest.getUsername(), tokenRequest.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody Utente utente) {
        try {
            Utente savedUtente = keycloakService.createUser(utente);
            return ResponseEntity.ok(savedUtente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user-info")
    public ResponseEntity<String> getUserInfo() {
        String userId = authenticationService.getUserId();
        return ResponseEntity.ok("User ID: " + userId);
    }

    @GetMapping("/utenti/{email}")
    public ResponseEntity<Utente> getUtenteByEmail(@PathVariable String email) {
        Utente utente = utenteService.getUtenteByEmail(email);
        if (utente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(utente);
    }


    @PutMapping("/utenti/{id}")
    public ResponseEntity<Utente> updateUtente(@PathVariable String id, @RequestBody Utente utenteDetails) {
        Utente updatedUtente = utenteService.updateUtente(id, utenteDetails);
        if (updatedUtente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUtente);
    }

    @DeleteMapping("/users/{email}")
    public ResponseEntity<Void> deleteUtente(@PathVariable String email) {
        try {
            utenteService.deleteUtente(email);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/userDetailsDataBase")
    public ResponseEntity<Utente> getUserDetailsDataBase() {
        try{
            return new ResponseEntity<>(utenteService.getUserDetailsDataBase(), HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkUserExists(@RequestParam String username) {
        boolean exists = utenteService.userExistsByUsername(username);
        return ResponseEntity.ok(exists);
    }

}


