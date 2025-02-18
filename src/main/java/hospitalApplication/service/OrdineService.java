package hospitalApplication.service;

import hospitalApplication.models.Ordine;
import hospitalApplication.models.StatoOrdine;
import hospitalApplication.repository.OrdineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdineService {

    private final OrdineRepository ordineRepository;

    public OrdineService(OrdineRepository ordineRepository) {
        this.ordineRepository = ordineRepository;
    }

    @Transactional
    public Ordine creaOrdine(String fornitore, String materiale, int quantita) {
        Ordine ordine = new Ordine();
        ordine.setFornitore(fornitore);
        ordine.setMateriale(materiale);
        ordine.setQuantita(quantita);
        ordine.setStato(StatoOrdine.IN_ATTESA);
        ordine.setDataOrdine(LocalDateTime.now());
        return ordineRepository.save(ordine);
    }

    public List<Ordine> getStoricoOrdini() {
        return ordineRepository.findAll();
    }

    public List<Ordine> getOrdiniInAttesa() {
        return ordineRepository.findByStato(StatoOrdine.IN_ATTESA);
    }

    @Transactional
    public Ordine aggiornaStatoOrdine(Long ordineId, StatoOrdine nuovoStato) {
        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new IllegalArgumentException("Ordine non trovato con ID: " + ordineId));
        ordine.setStato(nuovoStato);
        return ordineRepository.save(ordine);
    }
}
