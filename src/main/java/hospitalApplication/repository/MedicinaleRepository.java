package hospitalApplication.repository;

import feign.Param;
import hospitalApplication.models.Department;
import hospitalApplication.models.Medicinale;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicinaleRepository extends JpaRepository<Medicinale, Long> {
    List<Medicinale> findByDepartmentId(Long repartoId);

    List<Medicinale> findByQuantitaGreaterThan(int quantita);

    Optional<Medicinale> findByNome(String nomeMedicinale);


    @Modifying
    @Transactional
    @Query("UPDATE Medicinale m SET m.availableQuantity = :newAvailableQuantity WHERE m.id = :medicinaleId")
    int updateAvailableQuantity(@Param("medicinaleId") Long medicinaleId, @Param("newAvailableQuantity") int newAvailableQuantity);
}
