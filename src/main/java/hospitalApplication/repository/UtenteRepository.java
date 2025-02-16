package hospitalApplication.repository;
import hospitalApplication.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Utente findByEmail(String email);
    Utente findByUsername(String username);
    Utente findByFirstNameAndLastName(String firstName, String lastName);
    Optional<Utente> findByKeycloakId(String keycloakId);
    List<Utente> findByRepartoId(Long repartoId);

}

