package hospitalApplication.controller;

import hospitalApplication.models.Magazine;
import hospitalApplication.service.MagazineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/magazine")
@RequiredArgsConstructor
public class MagazineController {

    private final MagazineService magazineService;

    @PostMapping("/create")
    public ResponseEntity<Magazine> createMagazine(@RequestBody Magazine magazine) {
        Magazine savedMagazine = magazineService.createMagazine(magazine);
        return ResponseEntity.ok(savedMagazine);
    }

    @GetMapping("/stock")
    public ResponseEntity<Magazine> getStock() {
        return ResponseEntity.ok(magazineService.getStock());
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateStock(@RequestBody Magazine magazine) {
        magazineService.updateStock(magazine);
        return ResponseEntity.ok().build();
    }
}
