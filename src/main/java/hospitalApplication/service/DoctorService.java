package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.*;
import hospitalApplication.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DepartmentRepository departmentRepository;
    private final MedicinaleRepository medicinaleRepository;
    private final PazienteRepository pazienteRepository;
    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;
    private final SomministrazioneRepository somministrazioneRepository;

    public DoctorService(DepartmentRepository departmentRepository, MedicinaleRepository medicinaleRepository, PazienteRepository pazienteRepository, AuthenticationService authenticationService, UtenteRepository utenteRepository, SomministrazioneRepository  somministrazioneRepository) {
        this.departmentRepository = departmentRepository;
        this.medicinaleRepository = medicinaleRepository;
        this.pazienteRepository = pazienteRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
        this.somministrazioneRepository = somministrazioneRepository;

    }
    @Transactional
    public String somministraMedicine(Long pazienteId, String nomeMedicinale, int quantita) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Paziente paziente = pazienteRepository.findById(pazienteId)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        Medicinale medicinale = medicinaleRepository.findByNome(nomeMedicinale)
                .orElseThrow(() -> new RuntimeException("Medicinale non trovato"));

        if (medicinale.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Il medicinale non è più disponibile");
        }

        if (medicinale.getAvailableQuantity() < quantita) {
            return "Errore: Quantità insufficiente in magazzino!";
        }

        medicinale.setAvailableQuantity(medicinale.getAvailableQuantity() - quantita);
        medicinale.setQuantita(medicinale.getQuantita() - quantita);

        if (medicinale.getQuantita() <= 0) {
            medicinaleRepository.delete(medicinale);
            return "Medicinale esaurito ed eliminato dal magazzino!";
        } else {
            medicinaleRepository.save(medicinale);
        }

        Somministrazione somministrazione = new Somministrazione();
        somministrazione.setPaziente(paziente);
        somministrazione.setMedicinale(medicinale);
        somministrazione.setQuantita(quantita);
        somministrazione.setDataOra(LocalDateTime.now());
        somministrazioneRepository.save(somministrazione);

        return "Somministrati " + quantita + " unità di " + medicinale.getNome() + " al paziente " + paziente.getNome();
    }

    @Transactional
    public List<Paziente> getPazientiDelDottore() {
        String username = authenticationService.getUsername();
        Utente dottore = utenteRepository.findByUsername(username);
        if (dottore == null) {
            throw new IllegalArgumentException("Dottore non trovato");
        }

        List<Paziente> pazienti = pazienteRepository.findByDottore(dottore);
        System.out.println("Pazienti del dottore " + username + ": " + pazienti);
        return pazienti;
    }

    @Transactional
    public List<Medicinale> visualizzaMedicineReparto(Long repartoId) {
        List<Medicinale> medicinali = medicinaleRepository.findByDepartmentId(repartoId);

        medicinali.forEach(medicinale -> {
            medicinale.setDescrizione(null);
        });

        return medicinali;
    }

    @Transactional
    public List<DoctorDTO> getDottoriByReparto(Long repartoId) {
        return utenteRepository.findByRepartoId(repartoId).stream()
                .map(dottore -> new DoctorDTO(
                        dottore.getId(),
                        dottore.getFirstName(),
                        dottore.getLastName(),
                        dottore.getEmail(),
                        dottore.getNumeroMatricola()
                ))
                .collect(Collectors.toList());
    }

}
