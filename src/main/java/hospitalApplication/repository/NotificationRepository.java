package hospitalApplication.repository;

import feign.Param;
import hospitalApplication.models.Notification;
import hospitalApplication.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdAndLettaFalse(Long receiverId);
    List<Notification> findByReceiverId(Long userId);
    @Query("SELECT n FROM Notification n WHERE n.recipient = :chief AND n.sender.role = :role")
    List<Notification> findByRecipientAndSenderRole(@Param("chief") Utente chief, @Param("role") String role);

}

