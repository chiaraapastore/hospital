package com.example.hospital.service;


import com.example.hospital.config.AuthenticationService;
import com.example.hospital.models.Magazine;
import com.example.hospital.models.Medicinale;
import com.example.hospital.models.Utente;
import com.example.hospital.repository.MagazineRepository;
import com.example.hospital.repository.MedicinaleRepository;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class MagazineService {
    private final MagazineRepository magazineRepository;
    private final UtenteRepository utenteRepository;
    private final AuthenticationService authenticationService;
    private final MedicinaleRepository medicinaleRepository;

    public MagazineService(MagazineRepository magazineRepository,UtenteRepository utenteRepository, AuthenticationService authenticationService, MedicinaleRepository medicinaleRepository) {
        this.magazineRepository = magazineRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
        this.medicinaleRepository = medicinaleRepository;
    }


    public Magazine getUserStock() {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente autenticato non trovato");
        }

        String utenteIdAsString = String.valueOf(utente.getId());


        return magazineRepository.findByUtenteId(utenteIdAsString)
                .orElseThrow(() -> new IllegalArgumentException("Magazzino non trovato per l'utente"));
    }



    public void updateUserStock(Magazine magazine) {
        String username = authenticationService.getUsername();
        Utente utente = utenteRepository.findByUsername(username);
        if (utente == null) {
            throw new IllegalArgumentException("Utente autenticato non trovato");
        }

        magazine.setUtenteId(utente.getId());
        magazineRepository.save(magazine);
    }

    public boolean aggiornaScorte(String medicinaleId, int quantita) {
        Optional<Medicinale> medicinaleOpt = medicinaleRepository.findById(medicinaleId);

        if (medicinaleOpt.isPresent()) {
            Medicinale medicinale = medicinaleOpt.get();

            if (medicinale.getAvailableQuantity() >= quantita) {
                medicinale.setAvailableQuantity(medicinale.getAvailableQuantity() - quantita);
                medicinaleRepository.save(medicinale);
                return true;
            } else {
                return false;
            }
        } else {
            throw new RuntimeException("Medicinale con ID " + medicinaleId + " non trovato.");
        }
    }
}