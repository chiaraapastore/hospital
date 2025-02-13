package hospitalApplication.controller;

import hospitalApplication.models.Report;
import hospitalApplication.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        Report savedReport = reportService.createReport(report);
        return ResponseEntity.ok(savedReport);
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
