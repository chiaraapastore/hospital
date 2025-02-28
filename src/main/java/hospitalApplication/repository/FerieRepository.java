package hospitalApplication.repository;

import hospitalApplication.models.Ferie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FerieRepository extends JpaRepository<Ferie, Long> {
}
