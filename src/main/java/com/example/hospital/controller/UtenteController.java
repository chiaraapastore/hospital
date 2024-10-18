package com.example.hospital.controller;

import com.example.hospital.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utente")
public class UtenteController {

    private final KeycloakService keycloakService;

    @Autowired
    public UtenteController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }
}


