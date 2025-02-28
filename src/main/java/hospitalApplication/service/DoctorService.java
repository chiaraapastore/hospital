package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.*;
import hospitalApplication.repository.*;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    private final NotificationService notificationService;


    public DoctorService(DepartmentRepository departmentRepository, MedicinaleRepository medicinaleRepository, PazienteRepository pazienteRepository, AuthenticationService authenticationService, UtenteRepository utenteRepository, SomministrazioneRepository  somministrazioneRepository, NotificationService notificationService) {
        this.departmentRepository = departmentRepository;
        this.medicinaleRepository = medicinaleRepository;
        this.pazienteRepository = pazienteRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
        this.somministrazioneRepository = somministrazioneRepository;
        this.notificationService = notificationService;

    }
    @Transactional
    public Somministrazione somministraMedicine(Long pazienteId, Long capoRepartoId, String nomeMedicinale, int quantita) {
        try {
            Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
            if (utente == null) {
                throw new IllegalArgumentException("Utente non trovato");
            }

            Paziente paziente = pazienteRepository.findById(pazienteId)
                    .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

            Medicinale medicinale = medicinaleRepository.findByNome(nomeMedicinale)
                    .orElseThrow(() -> new RuntimeException("Medicinale non trovato"));

            Utente capoReparto = utenteRepository.findById(capoRepartoId)
                    .orElseThrow(() -> new RuntimeException("Capo Reparto non trovato"));

            if (medicinale.getAvailableQuantity() <= 0) {
                throw new RuntimeException("Il medicinale non è più disponibile");
            }

            if (medicinale.getAvailableQuantity() < quantita) {
                throw new RuntimeException("Quantità insufficiente in magazzino!");
            }

            medicinale.setAvailableQuantity(medicinale.getAvailableQuantity() - quantita);
            medicinale.setQuantita(medicinale.getQuantita() - quantita);

            if (medicinale.getQuantita() <= 0) {
                medicinaleRepository.delete(medicinale);
            } else {
                medicinaleRepository.save(medicinale);
            }

            Somministrazione somministrazione = new Somministrazione();
            somministrazione.setPaziente(paziente);
            somministrazione.setMedicinale(medicinale);
            somministrazione.setQuantita(quantita);
            somministrazione.setDataOra(LocalDateTime.now());
            somministrazioneRepository.save(somministrazione);

            notificationService.sendNotificationSomministration(utente, capoReparto, nomeMedicinale);

            return somministrazione;

        } catch (Exception e) {
            throw new RuntimeException("Errore durante la somministrazione: " + e.getMessage(), e);
        }
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
                        dottore.getNumeroMatricola(),
                        dottore.getRepartoNome()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public Department getRepartoByDottore(String emailDottore) {
      return utenteRepository.findRepartoByEmailDottore(emailDottore)
              .orElseThrow(() -> new RuntimeException("Nessun dottore trovato con email: " + emailDottore));
    }

    @Transactional
    public void scadenzaFarmaco(Long capoRepartoId, Long medicinaleId){
        Utente dottore = utenteRepository.findByUsername(authenticationService.getUsername());
        if (dottore == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        Utente capoReparto = utenteRepository.findById(capoRepartoId).orElseThrow(() -> new IllegalArgumentException("Capo Reparto non trovato"));
        Medicinale medicinale = medicinaleRepository.findById(medicinaleId)
                .orElseThrow(() -> new IllegalArgumentException("Medicinale non trovato"));

        notificationService.sendDoctorNotificationToCapoReparto(dottore,capoReparto, medicinale.getNome());

    }
}
