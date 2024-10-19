package com.example.hospital.service;
import com.example.hospital.entity.LoginRequest;
import com.example.hospital.entity.Utente;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;

    public List<Utente> findAll() {
        return utenteRepository.findAll();
    }

    public Utente save(Utente utente) {
        return utenteRepository.save(utente);
    }

    public Utente saveUser(Utente user) {
        return utenteRepository.save(user);
    }


    public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }

    public Utente getUtenteByEmail(String email) {
        return utenteRepository.findByEmail(email);
    }

    public Utente createUtente(Utente utente) {
        return utenteRepository.save(utente);
    }

    public Utente updateUtente(String id, Utente utenteDetails) {
        return utenteRepository.save(utenteDetails);
    }

    public boolean deleteUtente(String email) {
        return utenteRepository.delete(email);
    }

    public LoginRequest authenticate(LoginRequest loginRequest) {
        return loginRequest;
    }
}

