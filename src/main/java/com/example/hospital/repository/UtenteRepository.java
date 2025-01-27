package com.example.hospital.repository;

import com.example.hospital.models.Utente;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UtenteRepository extends MongoRepository<Utente, String> {
    Utente findByEmail(String username);
    void deleteByEmail(String email);

    Utente findByUsername(String username);
}
