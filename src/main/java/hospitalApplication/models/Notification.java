package hospitalApplication.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String messaggio;
    private boolean letta;
    private LocalDateTime dataOra;
    private String type;
    private LocalDateTime readNotification;
    private String destinatario;
    private String recipient;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Utente sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Utente receiver;
}
