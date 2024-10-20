package com.example.hospital.repository;

import com.example.hospital.entity.Utente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

public interface UtenteRepository extends MongoRepository<Utente, String> {
    static ClientRegistration findByRegistrationId(String clientId) {
        return null;
    }
    Utente findByEmail(String username);
    void deleteByEmail(String email);
}
