package com.example.hospital.controller;

import com.example.hospital.service.UserDepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utente-reparto")
public class UserDepartmentController {

    private final UserDepartmentService userDepartmentService;

    public UserDepartmentController(UserDepartmentService userDepartmentService) {
        this.userDepartmentService = userDepartmentService;
    }

    @GetMapping("/visualizza-referenze/{repartoId}")
    public ResponseEntity<String> visualizzaReferenzeReparto(@PathVariable String repartoId) {
        String response = userDepartmentService.visualizzaReferenzeReparto(repartoId);
        return ResponseEntity.ok(response);
    }
}
