package com.example.hospital.service;

import com.example.hospital.entity.Utente;
import com.example.hospital.utility.JwtUtils;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;


    @Autowired
    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }

    public Utente getUtenteByEmail(String email) {
        return utenteRepository.findByEmail(email);
    }

    public Utente createUtente(Utente utente) {
        return utenteRepository.save(utente); // Salva l'utente su MongoDB
    }

    public Utente updateUtente(String id, Utente utenteDetails) {
        Optional<Utente> optionalUtente = utenteRepository.findById(id);
        if (optionalUtente.isPresent()) {
            Utente utente = optionalUtente.get();
            utente.setFirstName(utenteDetails.getFirstName());
            return utenteRepository.save(utente);
        }
        return null;
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

