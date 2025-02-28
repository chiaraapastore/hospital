package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.*;
import hospitalApplication.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service

public class HeadOfDepartmentService {

    private MedicinaleRepository medicinaleRepository;
    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;
    private final NotificationService notificationService;
    private final DepartmentRepository departmentRepository;

    public HeadOfDepartmentService(MedicinaleRepository medicinaleRepository, UtenteRepository utenteRepository, AuthenticationService authenticationService, DepartmentRepository departmentRepository, NotificationService notificationService) {
        this.medicinaleRepository = medicinaleRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
        this.departmentRepository = departmentRepository;
        this.notificationService = notificationService;
    }


    @Transactional
    public void cambiaReparto(Long doctorId, Long nuovoRepartoId) {
        Utente user = getAuthenticatedUser();
        Utente dottore = utenteRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Dottore non trovato"));
        Department reparto = departmentRepository.findById(nuovoRepartoId).orElseThrow(() -> new IllegalArgumentException("Reparto non trovato"));
        dottore.setReparto(reparto);
        utenteRepository.save(dottore);
        notificationService.notifyDepartmentChange(dottore, reparto.getNome(), user);
    }



    @Transactional(readOnly = true)
    public List<Department> getReparti() {
        getAuthenticatedUser();
        return departmentRepository.findAll();
    }

    @Transactional
    public List<String> getFerieDisponibili() {
        return Arrays.asList(
                "2025-07-01", "2025-07-02", "2025-07-03",
                "2025-08-10", "2025-08-11", "2025-08-12",
                "2025-12-24", "2025-12-25", "2025-12-26"
        );
    }

    @Transactional
    public String aggiungiMedicinale(Medicinale medicinale) {
        getAuthenticatedUser();
        Department reparto = departmentRepository.findById(medicinale.getDepartment().getId())
                .orElseThrow(() -> new IllegalArgumentException("Reparto non trovato"));

        Medicinale nuovoMedicinale = new Medicinale();
        nuovoMedicinale.setNome(medicinale.getNome());
        nuovoMedicinale.setQuantita(medicinale.getQuantita());
        nuovoMedicinale.setDepartment(reparto);

        medicinaleRepository.save(nuovoMedicinale);
        return "Medicinale " + medicinale.getNome() + " aggiunto con successo al reparto " + reparto.getNome();
    }


    private Utente getAuthenticatedUser() {
        Utente user = utenteRepository.findByUsername(authenticationService.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
        return user;
    }
}
