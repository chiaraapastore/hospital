package com.example.hospital.repository;

import com.example.hospital.models.HeadOfDepartmentDTO;
import com.example.hospital.models.HeadOfDepartmentId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface HeadOfDepartmentRepository extends MongoRepository<HeadOfDepartmentDTO, HeadOfDepartmentId> {
}
