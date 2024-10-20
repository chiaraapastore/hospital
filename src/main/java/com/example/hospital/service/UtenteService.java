package com.example.hospital.service;
import com.example.hospital.entity.LoginRequest;
import com.example.hospital.entity.Utente;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Utente> optionalUtente = utenteRepository.findById(id);
        if (optionalUtente.isPresent()) {
            Utente utente = optionalUtente.get();
            utente.setName(utenteDetails.getName());
        }
        return utenteRepository.save(utenteDetails);
    }

    public LoginRequest authenticate(LoginRequest loginRequest) {
        return loginRequest;
    }

    public boolean deleteUtente(String email) {
        Utente optionalUtente = utenteRepository.findByEmail(email);
        if (optionalUtente != null) {
            utenteRepository.deleteByEmail(email);
            return true;
        }
        return false;
    }
}

