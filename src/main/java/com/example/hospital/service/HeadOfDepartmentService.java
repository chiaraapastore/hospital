package com.example.hospital.service;

import com.example.hospital.config.AuthenticationService;
import com.example.hospital.models.Department;
import com.example.hospital.models.Medicinale;
import com.example.hospital.models.Utente;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class HeadOfDepartmentService {
    private DepartmentRepository departmentRepository;
    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;

    public HeadOfDepartmentService(DepartmentRepository departmentRepository, UtenteRepository utenteRepository, AuthenticationService authenticationService) {
        this.departmentRepository = departmentRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
    }

    public String aggiornaScorteReparto(String repartoId, String medicinaleId, int nuovaQuantita) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Optional<Department> repartoOpt = departmentRepository.findById(repartoId);
        if (repartoOpt.isPresent()) {
            Department reparto = repartoOpt.get();
            List<Medicinale> scorte = reparto.getScorte();

            Optional<Medicinale> medicinaleOpt = scorte.stream()
                    .filter(r -> r.getId().equals(medicinaleId))
                    .findFirst();

            if (medicinaleOpt.isPresent()) {
                Medicinale medicinale = medicinaleOpt.get();
                medicinale.setQuantita(nuovaQuantita);
                departmentRepository.save(reparto);
                return "Scorte aggiornate per la referenza " + medicinaleId + " nel reparto " + repartoId;
            } else {
                throw new IllegalArgumentException("Referenza con ID " + medicinaleId + " non trovata nel reparto " + repartoId);
            }
        } else {
            throw new IllegalArgumentException("Reparto con ID " + repartoId + " non trovato");
        }
    }

    public String inviaNotifica(String repartoId, String messaggio) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        return "Notifica inviata al reparto " + repartoId + ": " + messaggio;
    }
}
