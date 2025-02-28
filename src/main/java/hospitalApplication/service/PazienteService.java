package hospitalApplication.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Department;
import hospitalApplication.models.Paziente;
import hospitalApplication.models.Somministrazione;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.DepartmentRepository;
import hospitalApplication.repository.PazienteRepository;
import hospitalApplication.repository.UtenteRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PazienteService {

    private final PazienteRepository pazienteRepository;
    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;
    private final DepartmentRepository departmentRepository;

    public PazienteService(PazienteRepository pazienteRepository, AuthenticationService authenticationService, UtenteRepository utenteRepository, DepartmentRepository departmentRepository) {
        this.pazienteRepository = pazienteRepository;
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Optional<Paziente> getPazienteById(Long id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return pazienteRepository.findById(id);
    }

    @Transactional
    public Paziente savePaziente(Paziente paziente) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        if (paziente.getDataRicovero() == null) {
            paziente.setDataRicovero(new Date());
        }
        return pazienteRepository.save(paziente);
    }

    @Transactional
    public void deletePaziente(Long id) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        pazienteRepository.deleteById(id);
    }
    @Transactional
    public List<Paziente> getAllPazienti() {
        return pazienteRepository.findAll();
    }

    @Transactional
    public byte[] generateCartellaClinicaPdf(Long pazienteId) throws DocumentException {
        Paziente paziente = pazienteRepository.findById(pazienteId)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 100);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();


        try {
            Image logo = Image.getInstance("https://i.pinimg.com/736x/f4/03/61/f40361e106a2512fa85f6fb59ba3f960.jpg");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(0, 121, 107));
        Paragraph title = new Paragraph("HospitalCare", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);


        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new BaseColor(0, 121, 107));
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        addTableRow(table, "Nome:", paziente.getNome(), headerFont, textFont);
        addTableRow(table, "Cognome:", paziente.getCognome(), headerFont, textFont);
        addTableRow(table, "Età:", String.valueOf(paziente.getEta()), headerFont, textFont);
        addTableRow(table, "Diagnosi:", paziente.getDiagnosi(), headerFont, textFont);
        addTableRow(table, "Email:", paziente.getEmail(), headerFont, textFont);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataRicovero = paziente.getDataRicovero() != null ? dateFormat.format(paziente.getDataRicovero()) : "N/A";
        addTableRow(table, "Data Ricovero:", dataRicovero, headerFont, textFont);

        String statoPaziente = paziente.isDimesso() ? "Dimesso" : "Ricoverato";
        addTableRow(table, "Stato:", statoPaziente, headerFont, textFont);

        document.add(table);
        document.add(new Paragraph("\n"));


        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new BaseColor(0, 121, 107));
        Paragraph farmaciTitle = new Paragraph("Farmaci Somministrati", sectionFont);
        farmaciTitle.setAlignment(Element.ALIGN_LEFT);
        document.add(farmaciTitle);

        List<Somministrazione> farmaci = paziente.getFarmaciSomministrati();
        if (farmaci == null || farmaci.isEmpty()) {
            document.add(new Paragraph("Nessun farmaco somministrato", textFont));
        } else {
            PdfPTable farmaciTable = new PdfPTable(2);
            farmaciTable.setWidthPercentage(100);
            addTableHeader(farmaciTable, "Nome Farmaco", "Data Somministrazione", headerFont);

            for (Somministrazione somministrazione : farmaci) {
                addTableRow(farmaciTable, somministrazione.getMedicinale().getNome(), somministrazione.getDataOra().toString(), textFont, textFont);
            }
            document.add(farmaciTable);
        }


        document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));


        Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(0, 102, 102));
        Paragraph footer = new Paragraph("Telefono: +39 012 345 6789 | Email: supporto@hospitalcare.it", footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
        return baos.toByteArray();
    }

    private void addTableRow(PdfPTable table, String header, String value, Font headerFont, Font textFont) {
        PdfPCell cell1 = new PdfPCell(new Phrase(header, headerFont));
        PdfPCell cell2 = new PdfPCell(new Phrase(value, textFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
    }

    private void addTableHeader(PdfPTable table, String header1, String header2, Font headerFont) {
        PdfPCell h1 = new PdfPCell(new Phrase(header1, headerFont));
        PdfPCell h2 = new PdfPCell(new Phrase(header2, headerFont));
        h1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        h2.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(h1);
        table.addCell(h2);
    }

    public Department getRepartoByPaziente(Long pazienteId) throws Exception {
        try {
            return pazienteRepository.findRepartoByPazienteId(pazienteId)
                    .orElseThrow(() -> new NotFoundException("Reparto non trovato per il paziente con ID: " + pazienteId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Errore nel recupero del reparto per il paziente con ID: " + pazienteId, e);
        }
    }

}