package com.example.hospital.service;

import com.example.hospital.config.AuthenticationService;
import com.example.hospital.models.Utente;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.stereotype.Service;




@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final AuthenticationService authenticationService;

    public UtenteService(UtenteRepository utenteRepository, AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
    }


    public Utente getUtenteByEmail(String email) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return utenteRepository.findByEmail(email);
    }


    public Utente updateUtente(String id, Utente utenteDetails) {
        Utente authenticatedUtente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (authenticatedUtente == null) {
            throw new IllegalArgumentException("Utente autenticato non trovato");
        }
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        utente.setFirstName(utenteDetails.getFirstName());
        utente.setLastName(utenteDetails.getLastName());

        return utenteRepository.save(utente);
    }

    public void deleteUtente(String email) {
        Utente utenteToDelete = utenteRepository.findByEmail(email);
        if (utenteToDelete == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        utenteRepository.delete(utenteToDelete);
    }


    public boolean userExistsByUsername(String username) {
        Utente user = utenteRepository.findByUsername(username);
        if (user != null) {
            return true;
        }else{
            return false;
        }
    }


    public Utente getUserDetailsDataBase() {
        String username = authenticationService.getUsername();
        return utenteRepository.findByUsername(username);
    }
}

