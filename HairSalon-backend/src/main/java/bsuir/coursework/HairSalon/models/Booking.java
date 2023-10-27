package bsuir.coursework.HairSalon.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;

@Entity
@Data
@Table(name = "bookings")
@Schema(description = "Entity representing a booking for a hair salon service")
public class Booking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "booking_id")
  @Schema(
    description = "Unique identifier for the booking",
    example = "1",
    hidden = true
  )
  private int id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @Schema(description = "User associated with the booking")
  private User user;

  @ManyToOne
  @JoinColumn(name = "service_id")
  @Schema(description = "Hair service associated with the booking")
  private HairService hairService;

  @ManyToOne
  @JoinColumn(name = "manager_id")
  @Schema(description = "User representing the service manager")
  private User serviceManager;

  @Column(name = "date_time", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @Schema(
    description = "Date and time of the booking",
    example = "2023-10-19T14:30:00Z"
  )
  private Date dateTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  @Schema(description = "Status of the booking", example = "RESERVED")
  private ServiceStatus status;

  public enum ServiceStatus {
    RESERVED,
    CONFIRMED,
    CANCELLED,
  }
}
