package hospitalApplication.service;


import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Magazine;
import hospitalApplication.models.Medicinale;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.MagazineRepository;
import hospitalApplication.repository.MedicinaleRepository;
import hospitalApplication.repository.UtenteRepository;
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


        return magazineRepository.findByUtenteId(utente.getId())
                .orElseThrow(() -> new IllegalArgumentException("Magazzino non trovato per l'utente"));
    }



    public void updateUserStock(Magazine magazine) {
        String username = authenticationService.getUsername();
        Utente utente = utenteRepository.findByUsername(username);
        if (utente == null) {
            throw new IllegalArgumentException("Utente autenticato non trovato");
        }

        magazineRepository.save(magazine);
    }

    public boolean aggiornaScorte(Long medicinaleId, int quantita) {
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