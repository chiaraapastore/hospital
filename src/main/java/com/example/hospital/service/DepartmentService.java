package com.example.hospital.service;

import com.example.hospital.config.AuthenticationService;
import com.example.hospital.models.Department;
import com.example.hospital.models.HeadOfDepartmentDTO;
import com.example.hospital.models.HeadOfDepartmentId;
import com.example.hospital.models.Utente;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.HeadOfDepartmentRepository;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final HeadOfDepartmentRepository headOfDepartmentRepository;
    private final UtenteRepository utenteRepository;
    private final AuthenticationService authenticationService;

    public DepartmentService(DepartmentRepository departmentRepository,HeadOfDepartmentRepository headOfDepartmentRepository, UtenteRepository utenteRepository, AuthenticationService authenticationService){
        this.departmentRepository = departmentRepository;
        this.headOfDepartmentRepository = headOfDepartmentRepository;
        this.utenteRepository = utenteRepository;
        this.authenticationService = authenticationService;
    }

    public List<Department> getAllDepartments() {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        return departmentRepository.findAll();
    }

    public Department createDepartment(Department department) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        return departmentRepository.save(department);
    }

    public void assignHeadOfDepartment(String departmentId, String capoRepartoId) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }


        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Reparto non trovato"));

        Utente capoReparto = utenteRepository.findById(capoRepartoId)
                .orElseThrow(() -> new RuntimeException("Capo reparto non trovato"));

        HeadOfDepartmentId headOfDepartmentId = new HeadOfDepartmentId(departmentId, capoRepartoId);
        HeadOfDepartmentDTO headOfDepartment = new HeadOfDepartmentDTO(headOfDepartmentId, department, capoReparto);

        headOfDepartmentRepository.save(headOfDepartment);
    }


    public Department updateDepartment(String id, Department updatedDepartment) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
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

        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        departmentRepository.deleteById(id);
    }
}
