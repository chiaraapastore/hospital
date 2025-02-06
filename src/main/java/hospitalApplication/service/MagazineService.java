package hospitalApplication.service;

import hospitalApplication.models.Magazine;
import hospitalApplication.models.Medicinale;
import hospitalApplication.repository.MagazineRepository;
import hospitalApplication.repository.MedicinaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MagazineService {
    private final MagazineRepository magazineRepository;
    private final MedicinaleRepository medicinaleRepository;

    public MagazineService(MagazineRepository magazineRepository, MedicinaleRepository medicinaleRepository) {
        this.magazineRepository = magazineRepository;
        this.medicinaleRepository = medicinaleRepository;
    }


    @Transactional
    public Magazine getStock() {
        return magazineRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Magazzino non trovato"));
    }

    @Transactional
    public Magazine createMagazine(Magazine magazine) {
        if (magazineRepository.count() > 0) {
            throw new IllegalStateException("Esiste già un magazzino. Non puoi crearne un altro.");
        }
        return magazineRepository.save(magazine);
    }


    @Transactional
    public void updateStock(Magazine magazine) {
        Magazine existingMagazine = getStock();
        existingMagazine.setStockDisponibile(magazine.getStockDisponibile());
        existingMagazine.setCapienzaMassima(magazine.getCapienzaMassima());
        magazineRepository.save(existingMagazine);
    }


    @Transactional
    public boolean aggiornaScorte(Long medicinaleId, int quantita) {
        Optional<Medicinale> medicinaleOpt = medicinaleRepository.findById(medicinaleId);

        if (medicinaleOpt.isPresent()) {
            Medicinale medicinale = medicinaleOpt.get();

            if (medicinale.getAvailableQuantity() >= quantita) {
                medicinale.setAvailableQuantity(medicinale.getAvailableQuantity() - quantita);
                medicinaleRepository.save(medicinale);
                return true;
            } else {
                return false;
            }
        } else {
            throw new RuntimeException("Medicinale con ID " + medicinaleId + " non trovato.");
        }
    }
}
