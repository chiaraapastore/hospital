package hospitalApplication.repository;
import feign.Param;
import hospitalApplication.models.Department;
import hospitalApplication.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Utente findByEmail(String email);
    Utente findByUsername(String username);
    Utente findByFirstNameAndLastName(String firstName, String lastName);
    Optional<Utente> findByKeycloakId(String keycloakId);
    List<Utente> findByRepartoId(Long repartoId);
    List<Utente> findByRole(String role);
    @Modifying
    @Query("UPDATE Utente u SET u.reparto = :reparto WHERE u.id = :utenteId")
    void aggiornaReparto(@Param("utenteId") Long utenteId, @Param("reparto") Department reparto);

    @Query("SELECT d.reparto FROM Utente d WHERE d.email = :emailDottore")
    Optional<Department> findRepartoByEmailDottore(@Param("emailDottore") String emailDottore);
}

