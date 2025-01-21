package com.example.hospital.repository;

import com.example.hospital.models.Magazine;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MagazineRepository extends MongoRepository<Magazine, String> {
}
