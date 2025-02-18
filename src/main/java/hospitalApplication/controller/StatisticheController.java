package hospitalApplication.controller;

import hospitalApplication.service.StatisticheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/statistiche")
public class StatisticheController {

    private final StatisticheService statisticheService;

    public StatisticheController(StatisticheService statisticheService) {
        this.statisticheService = statisticheService;
    }

    @GetMapping("/consumi")
    public ResponseEntity<Map<String, Object>> getConsumiNelTempo() {
        return ResponseEntity.ok(statisticheService.getConsumiNelTempo());
    }

    @GetMapping("/riordini")
    public ResponseEntity<Map<String, Object>> getRiordiniStockout() {
        return ResponseEntity.ok(statisticheService.getRiordiniStockout());
    }

    @GetMapping("/distribuzione-reparti")
    public ResponseEntity<List<Map<String, Object>>> getDistribuzionePerReparto() {
        return ResponseEntity.ok(statisticheService.getDistribuzionePerReparto());
    }

}
