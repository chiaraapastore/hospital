package com.example.hospital.service;


import com.example.hospital.models.Report;
import com.example.hospital.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public List<Report> generateReportsByQuery(String tipo, String inizio, String fine) {
        List<Report> allReports = reportRepository.findAll();
        return allReports.stream()
                .filter(report -> report.getTipo().equalsIgnoreCase(tipo))
                .filter(report -> report.getInizio().compareTo(inizio) >= 0)
                .filter(report -> report.getFine().compareTo(fine) <= 0)
                .collect(Collectors.toList());
    }
}
