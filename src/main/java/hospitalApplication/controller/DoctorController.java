package hospitalApplication.controller;

import hospitalApplication.models.Medicinale;
import hospitalApplication.models.Paziente;
import hospitalApplication.repository.MedicinaleRepository;
import hospitalApplication.repository.PazienteRepository;
import hospitalApplication.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/dottore")
public class DoctorController {

    private final DoctorService userDepartmentService;
    private final PazienteRepository pazienteRepository;
    private final MedicinaleRepository medicineRepository;
    private final DoctorService doctorService;

    public DoctorController(DoctorService userDepartmentService, PazienteRepository pazienteRepository, MedicinaleRepository medicineRepository, DoctorService doctorService) {
        this.userDepartmentService = userDepartmentService;
        this.pazienteRepository = pazienteRepository;
        this.medicineRepository = medicineRepository;
        this.doctorService = doctorService;

    }

    @GetMapping("/visualizza-medicine/{repartoId}")
    public ResponseEntity<List<Medicinale>> visualizzaMedicineReparto(@PathVariable Long repartoId) {
        List<Medicinale> medicinali = doctorService.visualizzaMedicineReparto(repartoId);
        return ResponseEntity.ok(medicinali);
    }



    @PostMapping("/somministra-medicine")
    public ResponseEntity<String> somministraMedicine(@RequestParam Long pazienteId, @RequestParam Long medicinaleId) {
        String response = userDepartmentService.somministraMedicine(pazienteId, medicinaleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pazienti")
    public ResponseEntity<List<Paziente>> getPazienti() {
        List<Paziente> pazienti = doctorService.getPazientiDelDottore();
        return ResponseEntity.ok(pazienti);
    }

}
