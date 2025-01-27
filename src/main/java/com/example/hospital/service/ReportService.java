package com.example.hospital.service;


import com.example.hospital.config.AuthenticationService;
import com.example.hospital.models.Report;
import com.example.hospital.models.Utente;
import com.example.hospital.repository.ReportRepository;
import com.example.hospital.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;

    public ReportService(ReportRepository reportRepository, AuthenticationService authenticationService, UtenteRepository utenteRepository){
        this.reportRepository = reportRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;

    }

    public List<Report> getAllReports() {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return reportRepository.findAll();
    }

    public Optional<Report> getReportById(String id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return reportRepository.findById(id);
    }

    public Report createReport(Report report) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        if (report.getDataInizio() == null) {
            report.setDataInizio(LocalDate.now());
        }
        if (report.getDataFine() == null) {
            report.setDataFine(LocalDate.now().plusDays(7));
        }
        return reportRepository.save(report);
    }


    public Report updateReport(String id, Report updatedReport) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return reportRepository.findById(id).map(existingReport -> {
            existingReport.setDataInizio(updatedReport.getDataInizio());
            existingReport.setDataFine(updatedReport.getDataFine());
            existingReport.setOra(updatedReport.getOra());
            return reportRepository.save(existingReport);
        }).orElseThrow(() -> new RuntimeException("Report not found"));
    }


}
