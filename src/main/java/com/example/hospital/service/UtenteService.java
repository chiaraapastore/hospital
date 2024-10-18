package com.example.hospital.service;
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


}

