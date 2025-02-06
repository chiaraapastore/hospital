package hospitalApplication.repository;
import hospitalApplication.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Utente findByEmail(String email);
    Utente findByUsername(String username);
    Utente findByFirstNameAndLastName(String firstName, String lastName);  // AGGIUNGI QUESTO
}

