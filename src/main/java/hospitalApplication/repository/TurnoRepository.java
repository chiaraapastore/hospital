package hospitalApplication.repository;

import hospitalApplication.models.Turni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepository extends JpaRepository<Turni, Long> {
}
