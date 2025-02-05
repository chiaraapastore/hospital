package hospitalApplication.repository;

import hospitalApplication.models.Medicinale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicinaleRepository extends JpaRepository<Medicinale, Long> {
    List<Medicinale> findByDepartmentId(Long repartoId);
}
