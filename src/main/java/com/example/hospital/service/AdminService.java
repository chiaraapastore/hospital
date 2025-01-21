package com.example.hospital.service;

import com.example.hospital.models.Department;
import com.example.hospital.models.Utente;
import com.example.hospital.repository.UtenteRepository;
import com.example.hospital.repository.DepartmentRepository;

import java.util.ArrayList;
import java.util.Optional;

public class AdminService {

    private UtenteRepository utenteRepository;
    private DepartmentRepository departmentRepository;

    public AdminService(UtenteRepository utenteRepository, DepartmentRepository departmentRepository) {
        this.utenteRepository = utenteRepository;
        this.departmentRepository = departmentRepository;
    }

    public String abilitaUtente(String userId) {
        Optional<Utente> utenteOpt = utenteRepository.findById(userId);
        if (utenteOpt.isPresent()) {
            Utente utente = utenteOpt.get();
            utente.setEnabled(true);
            utenteRepository.save(utente);
            return "Utente con ID " + userId + " abilitato con successo";
        } else {
            throw new IllegalArgumentException("Utente con ID " + userId + " non trovato");
        }
    }



    public String disabilitaUtente(String userId) {
        Optional<Utente> utenteOpt = utenteRepository.findById(userId);
        if (utenteOpt.isPresent()) {
            Utente utente = utenteOpt.get();
            utente.setEnabled(false);
            utenteRepository.save(utente);
            return "Utente con ID " + userId + " disabilitato con successo";
        } else {
            throw new IllegalArgumentException("Utente con ID " + userId + " non trovato");
        }
    }



    public String creaReparto(String nomeReparto) {
        Department reparto = new Department();
        reparto.setNome(nomeReparto);
        reparto.setScorte(new ArrayList<>());
        departmentRepository.save(reparto);
        return "Reparto " + nomeReparto + " creato con successo";
    }

}
