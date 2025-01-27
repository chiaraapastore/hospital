package com.example.hospital.repository;

import com.example.hospital.models.Paziente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PazienteRepository extends MongoRepository<Paziente, String> {
}
