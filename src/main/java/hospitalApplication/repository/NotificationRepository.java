package hospitalApplication.repository;

import hospitalApplication.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdAndLettaFalse(Long receiverId);
    List<Notification> findByReceiverId(Long userId);
}

