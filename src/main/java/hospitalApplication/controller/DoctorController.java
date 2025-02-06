package hospitalApplication.controller;

import hospitalApplication.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dottore")
public class DoctorController {

    private final DoctorService userDepartmentService;

    public DoctorController(DoctorService userDepartmentService) {
        this.userDepartmentService = userDepartmentService;
    }

    @GetMapping("/visualizza-medicine/{repartoId}")
    public ResponseEntity<String> visualizzaMedicineReparto(@PathVariable Long repartoId) {
        String response = userDepartmentService.visualizzaMedicineReparto(repartoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/somministra-medicine")
    public ResponseEntity<String> somministraMedicine(@RequestParam Long pazienteId, @RequestParam Long medicinaleId) {
        String response = userDepartmentService.somministraMedicine(pazienteId, medicinaleId);
        return ResponseEntity.ok(response);
    }


}
