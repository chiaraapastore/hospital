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

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable String id) {
        return reportService.getReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Report createReport(@RequestBody Report report) {
        return reportService.createReport(report);
    }

    @PutMapping("/update")
    public ResponseEntity<Report> updateReport(@PathVariable String id, @RequestBody Report updatedReport) {
        try {
            return ResponseEntity.ok(reportService.updateReport(id, updatedReport));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }



}
