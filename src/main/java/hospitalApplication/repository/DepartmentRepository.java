package hospitalApplication.repository;

import hospitalApplication.models.Department;
import hospitalApplication.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findFirstByNome(String nome);


    Optional<Department> findByCapoReparto(Utente utente);
}
