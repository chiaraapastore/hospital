package hospitalApplication.controller;

import hospitalApplication.models.*;
import hospitalApplication.repository.MedicinaleRepository;
import hospitalApplication.repository.PazienteRepository;
import hospitalApplication.service.DoctorService;
import hospitalApplication.service.HeadOfDepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/dottore")
public class DoctorController {

    private final DoctorService userDepartmentService;
    private final PazienteRepository pazienteRepository;
    private final MedicinaleRepository medicineRepository;
    private final DoctorService doctorService;
    private final HeadOfDepartmentService headOfDepartmentService;

    public DoctorController(DoctorService userDepartmentService, PazienteRepository pazienteRepository, MedicinaleRepository medicineRepository, DoctorService doctorService, HeadOfDepartmentService headOfDepartmentService) {
        this.userDepartmentService = userDepartmentService;
        this.pazienteRepository = pazienteRepository;
        this.medicineRepository = medicineRepository;
        this.doctorService = doctorService;
        this.headOfDepartmentService = headOfDepartmentService;

    }

    @GetMapping("/visualizza-medicine/{repartoId}")
    public ResponseEntity<List<Medicinale>> getMedicineByReparto(@PathVariable Long repartoId) {
        List<Medicinale> medicinali = doctorService.visualizzaMedicineReparto(repartoId);
        return ResponseEntity.ok(medicinali);
    }


    @PostMapping("/somministra-medicine")
    public ResponseEntity<Somministrazione> somministraMedicine(@RequestBody SomministrazioneRequest request) {
        System.out.println("Richiesta ricevuta:");
        System.out.println("Paziente ID: " + request.getPazienteId());
        System.out.println("Capo Reparto ID: " + request.getCapoRepartoId());
        System.out.println("Nome Medicinale: " + request.getNomeMedicinale());
        System.out.println("Quantità: " + request.getQuantita());

        Somministrazione response = doctorService.somministraMedicine(
                request.getPazienteId(),
                request.getCapoRepartoId(),
                request.getNomeMedicinale(),
                request.getQuantita()
        );

        System.out.println("Somministrazione effettuata con successo!");
        return ResponseEntity.ok(response);
    }






    @PostMapping("/scadenza")
    public ResponseEntity<?> scadenzaFarmaco( @PathVariable Long capoRepartoId, @PathVariable Long idMedicinale) {
        doctorService.scadenzaFarmaco(capoRepartoId, idMedicinale);
        return ResponseEntity.ok("notifica_inviata");
    }

    @GetMapping("/pazienti")
    public ResponseEntity<List<Paziente>> getPazienti() {
        List<Paziente> pazienti = doctorService.getPazientiDelDottore();
        return ResponseEntity.ok(pazienti);
    }

    @GetMapping("/reparto/{repartoId}")
    public ResponseEntity<List<DoctorDTO>> getDottoriByReparto(@PathVariable Long repartoId) {
        List<DoctorDTO> dottori = doctorService.getDottoriByReparto(repartoId).stream()
                .map(dottore -> new DoctorDTO(dottore.getId(), dottore.getFirstName(),
                        dottore.getLastName(), dottore.getEmail(), dottore.getMatricola(), dottore.getRepartoNome()))
                .collect(Collectors.toList());
        if (dottori.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dottori);
    }

    @GetMapping("/{emailDottore}/reparto")
    public ResponseEntity<Department> getRepartoByDottore(@PathVariable String emailDottore) {
        Department reparto = doctorService.getRepartoByDottore(emailDottore);
        return ResponseEntity.ok(reparto);
    }

    @PutMapping("/cambia-reparto/{doctorId}/{nuovoRepartoId}")
    public ResponseEntity<String> cambiaReparto(@PathVariable Long doctorId, @PathVariable Long nuovoRepartoId) {
        headOfDepartmentService.cambiaReparto(doctorId, nuovoRepartoId);
        return ResponseEntity.ok("Reparto cambiato con successo");
    }




}
