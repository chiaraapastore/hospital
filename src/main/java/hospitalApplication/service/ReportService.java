package hospitalApplication.service;


import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Report;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.ReportRepository;
import hospitalApplication.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public List<Report> getAllReports() {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return reportRepository.findAll();
    }

    @Transactional
    public Optional<Report> getReportById(String id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return reportRepository.findById(id);
    }

    @Transactional
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


    @Transactional
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
