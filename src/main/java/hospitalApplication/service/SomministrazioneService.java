package hospitalApplication.service;

import hospitalApplication.models.Paziente;
import hospitalApplication.models.Somministrazione;
import hospitalApplication.repository.PazienteRepository;
import hospitalApplication.repository.SomministrazioneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SomministrazioneService {


    private final SomministrazioneRepository somministrazioneRepository;

    private final MagazineService magazzinoService;

    private final PazienteRepository pazienteRepository;

    public SomministrazioneService(SomministrazioneRepository somministrazioneRepository, MagazineService magazzinoService, PazienteRepository pazienteRepository){
        this.magazzinoService = magazzinoService;
        this.somministrazioneRepository  = somministrazioneRepository;
        this.pazienteRepository = pazienteRepository;

    }

    @Transactional
    public Somministrazione registraSomministrazione(Somministrazione somministrazione) {
        boolean scorteAggiornate = magazzinoService.aggiornaScorte(somministrazione.getMedicinale().getId(), somministrazione.getQuantita());

        if (!scorteAggiornate) {
            notificaAnomalie(somministrazione.getMedicinale().getNome(), "Quantità insufficiente");
            throw new RuntimeException("Quantità insufficiente per il farmaco con Nome: " + somministrazione.getMedicinale().getNome());
        }

        somministrazione.setDataOra(LocalDateTime.now());
        return somministrazioneRepository.save(somministrazione);
    }

    public void notificaAnomalie(String referenzaId, String tipoAnomalia) {
        System.out.println("Notifica Anomalia - Referenza: " + referenzaId + ", Tipo: " + tipoAnomalia);
    }

    @Transactional
    public List<Somministrazione> getSomministrazioniByPaziente(Long pazienteId) {
        Paziente paziente = pazienteRepository.findById(pazienteId)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));
        return somministrazioneRepository.findByPaziente(paziente);
    }
}
