package hospitalApplication.repository;

import hospitalApplication.models.Medicinale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicinaleRepository extends JpaRepository<Medicinale, Long> {
    List<Medicinale> findByDepartmentId(Long repartoId);

    List<Medicinale> findByQuantitaGreaterThan(int quantita);

    Optional<Medicinale> findByNome(String nomeMedicinale);
}
