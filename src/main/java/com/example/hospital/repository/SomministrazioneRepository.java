package com.example.hospital.repository;

import com.example.hospital.models.Somministrazione;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface SomministrazioneRepository extends MongoRepository<Somministrazione, String> {
}
