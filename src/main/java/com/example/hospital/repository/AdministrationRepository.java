package com.example.hospital.repository;

import com.example.hospital.models.Administration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdministrationRepository extends MongoRepository<Administration, String> {
}
