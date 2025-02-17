package hospitalApplication.repository;

import hospitalApplication.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByNome(String repartoNome);
}
