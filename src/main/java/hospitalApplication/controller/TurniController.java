package hospitalApplication.controller;

import hospitalApplication.models.Turni;
import hospitalApplication.service.TurniService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/turni")
public class TurniController {

    private final TurniService turniService;

    public TurniController(TurniService turniService){
        this.turniService = turniService;
    }

    @PostMapping("create/turni")
    public ResponseEntity<Turni> createTurni(@RequestBody Turni turni, @PathVariable Long dottoreId){
        Turni turniAssegnati = turniService.assegnaTurno(dottoreId, turni.getInizioTurno(),turni.getFineTurno());
        return ResponseEntity.status(HttpStatus.CREATED).body(turniAssegnati);
    }
}