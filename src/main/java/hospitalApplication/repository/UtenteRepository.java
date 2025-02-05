package hospitalApplication.repository;
import hospitalApplication.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Utente findByEmail(String username);
    Utente findByUsername(String username);
}
