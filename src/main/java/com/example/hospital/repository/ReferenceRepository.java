package com.example.hospital.repository;

import com.example.hospital.models.Reference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceRepository extends MongoRepository<Reference, String> {}

