package hospitalApplication.repository;

import feign.Param;
import hospitalApplication.models.Paziente;
import hospitalApplication.models.Somministrazione;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SomministrazioneRepository extends JpaRepository<Somministrazione, String> {
    List<Somministrazione> findByPaziente(Paziente paziente);
    List<Somministrazione> findAll();
    @Query("SELECT s FROM Somministrazione s WHERE s.paziente.id = :pazienteId")
    List<Somministrazione> findByPazienteId(@Param("pazienteId") Long pazienteId);
}
