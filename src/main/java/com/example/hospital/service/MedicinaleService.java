package com.example.hospital.service;

import com.example.hospital.config.AuthenticationService;
import com.example.hospital.models.Medicinale;
import com.example.hospital.models.Utente;
import com.example.hospital.repository.MedicinaleRepository;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicinaleService {

    private final MedicinaleRepository medicinaleRepository;
    private final UtenteRepository utenteRepository;
    private final AuthenticationService authenticationService;

    public MedicinaleService(MedicinaleRepository medicinaleRepository, UtenteRepository utenteRepository, AuthenticationService authenticationService) {
        this.medicinaleRepository = medicinaleRepository;
        this.utenteRepository = utenteRepository;
        this.authenticationService = authenticationService;
    }

    public Optional<Medicinale> getMedicinaleById(String id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return medicinaleRepository.findById(id);
    }

    public Medicinale saveMedicinale(Medicinale medicinale) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return medicinaleRepository.save(medicinale);
    }

    public Medicinale updateMedicinaleQuantity(String medicinaleId, int newQuantity) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        Medicinale medicinale = medicinaleRepository.findById(medicinaleId)
                .orElseThrow(() -> new RuntimeException("Medicinale non trovato"));

        int currentQuantity = medicinale.getQuantita();

        int quantityDifference = newQuantity - currentQuantity;

        if (medicinale.getId() != null) {
            medicinale.setAvailableQuantity(medicinale.getAvailableQuantity() - quantityDifference);


            if (medicinale.getAvailableQuantity() <= 0) {
                System.out.println("Medicinale eliminato o segnalato per quantità esaurita: " + medicinale.getId());
                medicinaleRepository.delete(medicinale);
            } else {
                medicinaleRepository.save(medicinale);
            }
        }

        medicinale.setQuantita(newQuantity);

        if (newQuantity <= 0) {
            System.out.println("Referenza eliminata o segnalata per quantità esaurita: " + medicinale.getId());
            medicinaleRepository.delete(medicinale);
            return null;
        }

        return medicinaleRepository.save(medicinale);
    }

    public void deleteMedicinale(String id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        medicinaleRepository.deleteById(id);
    }

    public void verificaRiordino() {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        List<Medicinale> medicinali = medicinaleRepository.findAll();
        medicinali.stream()
                .filter(m -> m.getQuantita() <= m.getPuntoRiordino())
                .forEach(m -> System.out.println("Notifica: il medicinale " + m.getNome() + " ha raggiunto il livello di riordino."));
    }
}