package hospitalApplication.repository;

import hospitalApplication.models.Somministrazione;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SomministrazioneRepository extends JpaRepository<Somministrazione, String> {
}
