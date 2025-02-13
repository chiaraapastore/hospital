package hospitalApplication.controller;

import hospitalApplication.models.Paziente;
import hospitalApplication.service.AdminService;
import hospitalApplication.service.PazienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search/{id}")
    public ResponseEntity<Paziente> getPazienteById(@PathVariable Long id) {
        Optional<Paziente> paziente = pazienteService.getPazienteById(id);
        return paziente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<Paziente> savePaziente(@RequestBody Paziente paziente) {
        Paziente savedPaziente = pazienteService.savePaziente(paziente);
        return ResponseEntity.ok(savedPaziente);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePaziente(@PathVariable Long id) {
        pazienteService.deletePaziente(id);
        return ResponseEntity.noContent().build();
    }
}
