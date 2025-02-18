package hospitalApplication.repository;

import hospitalApplication.models.Paziente;
import hospitalApplication.models.Somministrazione;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SomministrazioneRepository extends JpaRepository<Somministrazione, String> {
    List<Somministrazione> findByPaziente(Paziente paziente);
    List<Somministrazione> findAll();
}
