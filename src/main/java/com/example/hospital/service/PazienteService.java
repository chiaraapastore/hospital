package com.example.hospital.service;

import com.example.hospital.config.AuthenticationService;
import com.example.hospital.models.Paziente;
import com.example.hospital.models.Utente;
import com.example.hospital.repository.PazienteRepository;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PazienteService {

    private final PazienteRepository pazienteRepository;
    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;

    public PazienteService(PazienteRepository pazienteRepository, AuthenticationService authenticationService, UtenteRepository utenteRepository) {
        this.pazienteRepository = pazienteRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
    }

    public Optional<Paziente> getPazienteById(String id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return pazienteRepository.findById(id);
    }

    public Paziente savePaziente(Paziente paziente) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return pazienteRepository.save(paziente);
    }

    public void deletePaziente(String id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        pazienteRepository.deleteById(id);
    }
}
