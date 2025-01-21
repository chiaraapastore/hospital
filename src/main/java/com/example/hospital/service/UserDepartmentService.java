package com.example.hospital.service;

import com.example.hospital.models.Department;
import com.example.hospital.models.Reference;
import com.example.hospital.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

public class UserDepartmentService {

    private final DepartmentRepository departmentRepository;
    public UserDepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    public String visualizzaReferenzeReparto(String repartoId) {
        Optional<Department> repartoOpt = departmentRepository.findById(repartoId);
        if (repartoOpt.isPresent()) {
            Department reparto = repartoOpt.get();
            List<Reference> referenze = reparto.getScorte();
            return "Referenze disponibili per il reparto con ID " + repartoId + ": " + referenze;
        } else {
            throw new IllegalArgumentException("Reparto con ID " + repartoId + " non trovato");
        }
    }

}
