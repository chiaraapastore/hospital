package com.example.hospital.controller;

import com.example.hospital.models.Report;
import com.example.hospital.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/?tipo=<tipo>&inizio=<inizio>&fine=<fine>")
    public ResponseEntity<List<Report>> generateReports(
            @RequestParam("tipo") String tipo,
            @RequestParam("inizio") String inizio,
            @RequestParam("fine") String fine) {
        return ResponseEntity.ok(reportService.generateReportsByQuery(tipo, inizio, fine));
    }

    @PostMapping("/create")
    public ResponseEntity<Report> generateReport(@RequestBody Report report) {
        return ResponseEntity.ok(reportService.saveReport(report));
    }

    @GetMapping("/listaReports")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }
}
