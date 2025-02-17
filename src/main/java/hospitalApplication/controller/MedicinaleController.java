package hospitalApplication.controller;

import hospitalApplication.models.Medicinale;
import hospitalApplication.repository.MedicinaleRepository;
import hospitalApplication.service.MedicinaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/medicinali")
public class MedicinaleController {

    private final MedicinaleService medicinaleService;
    private final MedicinaleRepository medicinaleRepository;

    public MedicinaleController(MedicinaleService medicinaleService, MedicinaleRepository medicinaleRepository) {
        this.medicinaleService = medicinaleService;
        this.medicinaleRepository = medicinaleRepository;
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Medicinale> getMedicinaleById(@PathVariable Long id) {
        Optional<Medicinale> medicinale = medicinaleService.getMedicinaleById(id);
        return medicinale.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<Medicinale> saveMedicinale(@RequestBody Medicinale medicinale) {
        Medicinale savedMedicinale = medicinaleService.saveMedicinale(medicinale);
        return ResponseEntity.ok(savedMedicinale);
    }

    @PutMapping("/{id}/update-available-quantity")
    public ResponseEntity<Medicinale> updateMedicinaleAvailableQuantity(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {

        int availableQuantity = request.get("availableQuantity");

        medicinaleService.updateMedicinaleAvailableQuantity(id, availableQuantity);

        Medicinale updated = medicinaleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicinale non trovato dopo l'aggiornamento!"));

        System.out.println("💡 Backend sta restituendo available_quantity: " + updated.getAvailableQuantity());

        return ResponseEntity.ok(updated);
    }




    @PutMapping("/{id}")
    public ResponseEntity<Medicinale> updateMedicinale(@PathVariable Long id, @RequestBody Medicinale medicinale) {
        Optional<Medicinale> existingMedicinale = medicinaleRepository.findById(id);
        if (existingMedicinale.isPresent()) {
            Medicinale updated = existingMedicinale.get();
            updated.setQuantita(medicinale.getQuantita());
            updated.setScadenza(medicinale.getScadenza());
            updated.setCategoria(medicinale.getCategoria());
            medicinaleRepository.save(updated);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}/")
    public ResponseEntity<Void> deleteMedicinale(@PathVariable Long id) {
        medicinaleService.deleteMedicinale(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/check-reorder")
    public ResponseEntity<String> checkReorderPoint(@PathVariable Long id) {
        Optional<Medicinale> medicinaleOpt = medicinaleService.getMedicinaleById(id);

        if (medicinaleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Medicinale medicinale = medicinaleOpt.get();
        int reorderPoint = medicinale.getPuntoRiordino();
        int currentQuantity = medicinale.getQuantita();

        if (currentQuantity <= reorderPoint) {
            return ResponseEntity.ok("È necessario riordinare il medicinale: " + medicinale.getNome());
        }

        return ResponseEntity.ok("Il medicinale: " + medicinale.getNome() + " ha una quantità sufficiente.");
    }

    @GetMapping("/disponibili")
    public ResponseEntity<List<Medicinale>> getMedicinaliDisponibili() {
        List<Medicinale> medicinali = medicinaleService.getMedicinaliDisponibili();
        return ResponseEntity.ok(medicinali);
    }


}