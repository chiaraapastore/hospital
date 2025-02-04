package com.example.hospital.repository;

import com.example.hospital.models.Medicinale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicinaleRepository extends MongoRepository<Medicinale, String> {
}
