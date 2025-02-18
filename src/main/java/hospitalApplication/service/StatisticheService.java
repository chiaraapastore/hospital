package hospitalApplication.service;

import hospitalApplication.models.Somministrazione;
import hospitalApplication.repository.MedicinaleRepository;
import hospitalApplication.repository.SomministrazioneRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticheService {

    private final SomministrazioneRepository somministrazioneRepository;
    private final MedicinaleRepository medicinaleRepository;

    public StatisticheService(SomministrazioneRepository somministrazioneRepository, MedicinaleRepository medicinaleRepository) {
        this.somministrazioneRepository = somministrazioneRepository;
        this.medicinaleRepository = medicinaleRepository;
    }

    @Transactional
    public Map<String, Object> getConsumiNelTempo() {
        List<Somministrazione> somministrazioni = somministrazioneRepository.findAll();

        Map<String, Integer> consumiPerData = somministrazioni.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getDataOra().toLocalDate().toString(),
                        Collectors.summingInt(Somministrazione::getQuantita)
                ));

        List<Map<String, Object>> series = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : consumiPerData.entrySet()) {
            series.add(Map.of("name", entry.getKey(), "value", entry.getValue()));
        }

        return Map.of("name", "Farmaci Somministrati", "series", series);
    }

    @Transactional
    public Map<String, Object> getRiordiniStockout() {
        long totaleRiordini = medicinaleRepository.count();
        long stockout = medicinaleRepository.findByQuantitaLessThanEqual(0).size();

        return Map.of(
                "riordini", Map.of("name", "Riordini", "value", totaleRiordini),
                "stockout", Map.of("name", "Stockout", "value", stockout)
        );
    }

    @Transactional

    public List<Map<String, Object>> getDistribuzionePerReparto() {
        List<Map<String, Object>> risultato = medicinaleRepository.findConsumoPerReparto();

        System.out.println("Distribuzione per reparto generata: " + risultato);

        return risultato;
    }

}
