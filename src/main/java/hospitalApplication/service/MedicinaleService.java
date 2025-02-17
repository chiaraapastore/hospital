package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Medicinale;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.DepartmentRepository;
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
    private final DepartmentService  departmentService;
    private final DepartmentRepository departmentRepository;

    public MedicinaleService(MedicinaleRepository medicinaleRepository, UtenteRepository utenteRepository, AuthenticationService authenticationService, DepartmentService departmentService, DepartmentRepository departmentRepository) {
        this.medicinaleRepository = medicinaleRepository;
        this.utenteRepository = utenteRepository;
        this.authenticationService = authenticationService;
        this.departmentService = departmentService;
        this.departmentRepository = departmentRepository;
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
    public void updateMedicinaleAvailableQuantity(Long medicinaleId, int newAvailableQuantity) {
        int updatedRows = medicinaleRepository.updateAvailableQuantity(medicinaleId, newAvailableQuantity);
        if (updatedRows == 0) {
            throw new RuntimeException("Nessuna riga aggiornata! Controlla se l'ID esiste.");
        }
        System.out.println("available_quantity aggiornato con query diretta nel DB!");
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