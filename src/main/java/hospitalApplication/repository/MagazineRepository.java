package hospitalApplication.repository;


import hospitalApplication.models.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MagazineRepository extends JpaRepository<Magazine, Long> {

}
