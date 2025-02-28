package hospitalApplication.controller;

import hospitalApplication.models.Ferie;
import hospitalApplication.service.FerieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ferie")
public class FerieController {

    private final FerieService ferieService;

    public FerieController(FerieService ferieService){
        this.ferieService = ferieService;
    }

    @PostMapping("/create")
    public ResponseEntity<Ferie> createFerie(@RequestBody Ferie ferie, @PathVariable Long capoRepartoId) {
        Ferie ferieAssegnate = ferieService.assegnaFerie(capoRepartoId,ferie.getInizioFerie(), ferie.getFineFerie());
        return ResponseEntity.status(HttpStatus.CREATED).body(ferieAssegnate);
    }
}
