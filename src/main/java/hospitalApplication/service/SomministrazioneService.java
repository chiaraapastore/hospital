package hospitalApplication.service;

import hospitalApplication.models.Somministrazione;
import hospitalApplication.repository.SomministrazioneRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SomministrazioneService {


    private final SomministrazioneRepository somministrazioneRepository;

    private final MagazineService magazzinoService;

    public SomministrazioneService(SomministrazioneRepository somministrazioneRepository, MagazineService magazzinoService){
        this.magazzinoService = magazzinoService;
        this.somministrazioneRepository  = somministrazioneRepository;

    }

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
}
