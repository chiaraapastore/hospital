package hospitalApplication.repository;

import hospitalApplication.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, String> {
}

