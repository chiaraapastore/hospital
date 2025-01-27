package com.example.hospital.service;

import com.example.hospital.config.AuthenticationService;
import com.example.hospital.models.*;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.MedicinaleRepository;
import com.example.hospital.repository.PazienteRepository;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DepartmentRepository departmentRepository;
    private final MedicinaleRepository medicinaleRepository;
    private final PazienteRepository pazienteRepository;
    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;

    public DoctorService(DepartmentRepository departmentRepository, MedicinaleRepository medicinaleRepository, PazienteRepository pazienteRepository, AuthenticationService authenticationService, UtenteRepository utenteRepository) {
        this.departmentRepository = departmentRepository;
        this.medicinaleRepository = medicinaleRepository;
        this.pazienteRepository = pazienteRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
    }
    public String visualizzaReferenzeReparto(String repartoId) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Optional<Department> repartoOpt = departmentRepository.findById(repartoId);
        if (repartoOpt.isPresent()) {
            Department reparto = repartoOpt.get();
            List<Medicinale> medicinale = reparto.getScorte();
            return "Referenze disponibili per il reparto con ID " + repartoId + ": " + medicinale;
        } else {
            throw new IllegalArgumentException("Reparto con ID " + repartoId + " non trovato");
        }
    }

    public String somministraMedicine(String pazienteId, String medicinaleId) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Paziente paziente = pazienteRepository.findById(pazienteId)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        Medicinale medicinale = medicinaleRepository.findById(medicinaleId)
                .orElseThrow(() -> new RuntimeException("Medicinale non trovato"));

        if (medicinale.getAvailableQuantity()<= 0) {
            throw new RuntimeException("Il medicinale non è più disponibile");
        }

        medicinale.setAvailableQuantity(medicinale.getAvailableQuantity() - 1);
        medicinaleRepository.save(medicinale);

        return "Medicine somministrata con successo al paziente " + paziente.getNome() +
                " con il medicinale " + medicinale.getNome();
    }
}
