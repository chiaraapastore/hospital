package hospitalApplication.controller;

import hospitalApplication.models.Magazine;
import hospitalApplication.service.MagazineService;
import hospitalApplication.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/magazine")
@RequiredArgsConstructor
public class MagazineController {

    private final MagazineService magazineService;
    private final ReportService reportService;

    @PostMapping("/create")
    public ResponseEntity<Magazine> createMagazine(@RequestBody Magazine magazine) {
        Magazine savedMagazine = magazineService.createMagazine(magazine);
        return ResponseEntity.ok(savedMagazine);
    }

    @GetMapping("/stock")
    public ResponseEntity<Magazine> getStock() {
        return ResponseEntity.ok(magazineService.getStock());
    }

    @PutMapping("/update-stock-and-report")
    public ResponseEntity<Void> updateStockAndSendReport(@RequestBody Magazine magazine) {
        System.out.println("Dati ricevuti dal frontend: " + magazine);

        if (magazine == null || magazine.getId() == null) {
            System.err.println("Errore: Magazine nullo o senza ID!");
            return ResponseEntity.badRequest().build();
        }

        try {
            magazineService.updateStock(magazine);
            reportService.generateStockReport(magazine);
            System.out.println("Stock aggiornato e report inviato!");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Errore nel backend: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



}
