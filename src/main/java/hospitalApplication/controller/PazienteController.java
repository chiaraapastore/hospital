package hospitalApplication.controller;

import com.itextpdf.text.DocumentException;
import hospitalApplication.models.Department;
import hospitalApplication.models.Paziente;
import hospitalApplication.service.AdminService;
import hospitalApplication.service.DoctorService;
import hospitalApplication.service.PazienteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/pazienti")
public class PazienteController {

    private final PazienteService pazienteService;

    public PazienteController(PazienteService pazienteService) {
        this.pazienteService = pazienteService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Paziente>> getAllPazienti() {
        List<Paziente> pazienti = pazienteService.getAllPazienti();
        return ResponseEntity.ok(pazienti);
    }

    @GetMapping("/{pazienteId}/reparto")
    public ResponseEntity<Department> getRepartoByPaziente(@PathVariable Long pazienteId) throws Exception {
        Department reparto = pazienteService.getRepartoByPaziente(pazienteId);
        return ResponseEntity.ok(reparto);
    }


    @GetMapping("/search/{id}")
    public ResponseEntity<Paziente> getPazienteById(@PathVariable Long id) {
        Optional<Paziente> paziente = pazienteService.getPazienteById(id);
        return paziente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }



    @PostMapping("/save")
    public ResponseEntity<Paziente> savePaziente(@RequestBody Paziente paziente) {
        if (paziente.getDataRicovero() == null) {
            paziente.setDataRicovero(new Date());
        }
        Paziente savedPaziente = pazienteService.savePaziente(paziente);
        return ResponseEntity.ok(savedPaziente);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePaziente(@PathVariable Long id) {
        pazienteService.deletePaziente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/cartella-clinica")
    public ResponseEntity<byte[]> getCartellaClinicaPdf(@PathVariable Long id) throws DocumentException {
        byte[] pdfBytes = pazienteService.generateCartellaClinicaPdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cartella_clinica.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
