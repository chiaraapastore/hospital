package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Medicinale;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.MedicinaleRepository;
import hospitalApplication.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Optional<Medicinale> getMedicinaleById(Long id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return medicinaleRepository.findById(id);
    }

    @Transactional
    public Medicinale saveMedicinale(Medicinale medicinale) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return medicinaleRepository.save(medicinale);
    }

    @Transactional
    public Medicinale updateMedicinaleQuantity(Long medicinaleId, int newQuantity) {
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

    @Transactional
    public void deleteMedicinale(Long id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        medicinaleRepository.deleteById(id);
    }

    public List<Medicinale> getMedicinaliDisponibili() {
        return medicinaleRepository.findByQuantitaGreaterThan(0);
    }
}