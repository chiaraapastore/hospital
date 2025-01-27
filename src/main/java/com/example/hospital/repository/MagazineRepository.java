package com.example.hospital.repository;

import com.example.hospital.models.Magazine;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MagazineRepository extends MongoRepository<Magazine, String> {
    Optional<Magazine> findByUtenteId(String utenteId);
}
