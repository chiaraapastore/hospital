package hospitalApplication.repository;

import feign.Param;
import hospitalApplication.models.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {
    @Query("SELECT m FROM Magazine m WHERE m.utente.id = :utenteId")
    Optional<Magazine> findByUtenteId(@Param("utenteId") Long utenteId);

}
