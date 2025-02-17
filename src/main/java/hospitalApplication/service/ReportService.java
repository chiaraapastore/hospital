package hospitalApplication.service;


import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.*;
import hospitalApplication.repository.PazienteRepository;
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
    private final NotificationService notificationService;
    private final PazienteRepository pazienteRepository;

    public ReportService(ReportRepository reportRepository, AuthenticationService authenticationService, UtenteRepository utenteRepository, NotificationService notificationService, PazienteRepository pazienteRepository){
        this.reportRepository = reportRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
        this.notificationService = notificationService;
        this.pazienteRepository = pazienteRepository;

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


    @Transactional
    public void generateStockReport(Magazine magazine) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Errore: Utente non trovato!");
        }


        Paziente paziente = pazienteRepository.findFirstByOrderByIdAsc();
        if (paziente == null) {
            System.err.println(" Errore: Nessun paziente trovato nel sistema! Il report non verrà salvato.");
            return;
        }

        String reportMessage = "📦 Report Magazzino\n" +
                "Stock Disponibile: " + magazine.getStockDisponibile() + "\n" +
                "Capienza Massima: " + magazine.getCapienzaMassima() + "\n" +
                (magazine.getStockDisponibile() < magazine.getPuntoRiordino() ? " Scorte sotto il punto di riordino!" : "Scorte sufficienti.");

        Report report = new Report();
        report.setDataInizio(LocalDate.now());
        report.setDataFine(LocalDate.now().plusDays(7));
        report.setAutore(utente);
        report.setPaziente(paziente);
        reportRepository.save(report);

        Utente adminUser = utenteRepository.findByUsername("admin");
        if (adminUser != null) {
            notificationService.createAndSendNotification(utente, adminUser, reportMessage, "stock_report");
            System.out.println("Report inviato all'admin: \n" + reportMessage);
        } else {
            System.err.println("Errore: Utente admin non trovato! Notifica non inviata.");
        }
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
}
