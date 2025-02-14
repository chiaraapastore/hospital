package hospitalApplication.repository;

import feign.Param;
import hospitalApplication.models.Paziente;
import hospitalApplication.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PazienteRepository extends JpaRepository<Paziente, Long> {
    List<Paziente> findByDottore(Utente dottore);
    @Query("SELECT p FROM Paziente p LEFT JOIN FETCH p.farmaciSomministrati WHERE p.id = :id")
    Optional<Paziente> findByIdWithFarmaciSomministrati(@Param("id") Long id);

}
