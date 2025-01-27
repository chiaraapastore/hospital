package com.example.hospital.service;

import com.example.hospital.config.AuthenticationService;
import com.example.hospital.models.Department;
import com.example.hospital.models.Utente;
import com.example.hospital.repository.UtenteRepository;
import com.example.hospital.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service

public class AdminService {

    private UtenteRepository utenteRepository;
    private DepartmentRepository departmentRepository;
    private final AuthenticationService authenticationService;

    public AdminService(UtenteRepository utenteRepository, DepartmentRepository departmentRepository, AuthenticationService authenticationService) {
        this.departmentRepository = departmentRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
    }

    public String creaReparto(String nomeReparto) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Department reparto = new Department();
        reparto.setNome(nomeReparto);
        reparto.setScorte(new ArrayList<>());
        departmentRepository.save(reparto);
        return "Reparto " + nomeReparto + " creato con successo";
    }

    public String aggiungiUtenteAReparto(String utenteId, String repartoId) {

        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Optional<Utente> utenteOpt = utenteRepository.findById(utenteId);
        Optional<Department> repartoOpt = departmentRepository.findById(repartoId);

        if (utenteOpt.isPresent() && repartoOpt.isPresent()) {
            Utente utenteToAdd = utenteOpt.get();
            Department reparto = repartoOpt.get();

            reparto.getUtenti().add(utenteToAdd);
            departmentRepository.save(reparto);

            return "Utente " + utenteToAdd.getId() + " aggiunto al reparto " + reparto.getNome();
        } else {
            return "Utente o reparto non trovato";
        }
    }


    public String assegnaCapoReparto(String utenteId, String repartoId) {

        Utente authenticatedUser = utenteRepository.findByUsername(authenticationService.getUsername());
        if (authenticatedUser == null) {
            throw new IllegalArgumentException("Utente autenticato non trovato");
        }

        Optional<Utente> utenteOpt = utenteRepository.findById(utenteId);
        Optional<Department> repartoOpt = departmentRepository.findById(repartoId);

        if (utenteOpt.isPresent() && repartoOpt.isPresent()) {
            Utente utente = utenteOpt.get();
            Department reparto = repartoOpt.get();

            reparto.setCapoReparto(utente);
            departmentRepository.save(reparto);

            return "Utente " + utente.getId() + " assegnato come capo del reparto " + reparto.getNome();
        } else {
            return "Utente o reparto non trovato";
        }
    }


}
