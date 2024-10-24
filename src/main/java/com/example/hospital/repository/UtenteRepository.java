package com.example.hospital.repository;

import com.example.hospital.entity.Utente;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UtenteRepository extends MongoRepository<Utente, String> {
   /* static ClientRegistration findByRegistrationId(String clientId) {
        return null;
    }
    */
    Utente findByEmail(String username);
    void deleteByEmail(String email);

    List<Utente> findByUsername(String username);
}
