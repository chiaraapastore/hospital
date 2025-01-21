package com.example.hospital.service;

import com.example.hospital.models.Department;
import com.example.hospital.models.HeadOfDepartmentDTO;
import com.example.hospital.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public void assignHeadOfDepartment(String departmentId, HeadOfDepartmentDTO capoRepartoId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Reparto non trovato"));
        department.setCapoReparto(capoRepartoId);
        departmentRepository.save(department);
    }


    public Department updateDepartment(String id, Department updatedDepartment) {
        Optional<Department> existing = departmentRepository.findById(id);
        if (existing.isPresent()) {
            Department department = existing.get();
            department.setNome(updatedDepartment.getNome());
            department.setCapoReparto(updatedDepartment.getCapoReparto());
            department.setScorte(updatedDepartment.getScorte());
            return departmentRepository.save(department);
        }
        throw new RuntimeException("Dipartimento non trovato");
    }

    public void deleteDepartment(String id) {
        departmentRepository.deleteById(id);
    }
}
