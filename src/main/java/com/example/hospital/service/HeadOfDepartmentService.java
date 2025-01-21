package com.example.hospital.service;

import com.example.hospital.models.Department;
import com.example.hospital.models.Reference;
import com.example.hospital.repository.DepartmentRepository;
import java.util.List;
import java.util.Optional;

public class HeadOfDepartmentService {
    private DepartmentRepository departmentRepository;
    public HeadOfDepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    public String aggiornaScorteReparto(String repartoId, String referenzaId, int nuovaQuantita) {
        Optional<Department> repartoOpt = departmentRepository.findById(repartoId);
        if (repartoOpt.isPresent()) {
            Department reparto = repartoOpt.get();
            List<Reference> scorte = reparto.getScorte();

            Optional<Reference> referenzaOpt = scorte.stream()
                    .filter(r -> r.getId().equals(referenzaId))
                    .findFirst();

            if (referenzaOpt.isPresent()) {
                Reference referenza = referenzaOpt.get();
                referenza.setQuantita(nuovaQuantita);
                departmentRepository.save(reparto);
                return "Scorte aggiornate per la referenza " + referenzaId + " nel reparto " + repartoId;
            } else {
                throw new IllegalArgumentException("Referenza con ID " + referenzaId + " non trovata nel reparto " + repartoId);
            }
        } else {
            throw new IllegalArgumentException("Reparto con ID " + repartoId + " non trovato");
        }
    }

}
