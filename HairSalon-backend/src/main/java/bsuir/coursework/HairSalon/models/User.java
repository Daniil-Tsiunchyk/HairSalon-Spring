package bsuir.coursework.HairSalon.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
@Schema(description = "Entity representing a user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  @Schema(description = "Unique identifier for the user", example = "1")
  private int id;

  @Column(unique = true, nullable = false)
  @Schema(description = "Username of the user", example = "john_doe")
  private String username;

  @Column(unique = true)
  @Schema(
    description = "Email address of the user",
    example = "john.doe@example.com"
  )
  private String email;

  @Column(nullable = false)
  @Schema(description = "Password of the user", example = "password123")
  private String password;

  @Column
  @Schema(description = "First name of the user", example = "Daniil")
  private String firstName;

  @Column
  @Schema(description = "Last name of the user", example = "Makarov")
  private String lastName;

  @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
  @JoinColumn(name = "user_id")
  @Schema(description = "Discount card associated with the user")
  @JsonIgnore
  private DiscountCard discountCard;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Schema(description = "Bookings associated with the user")
  @JsonIgnore
  private List<Booking> bookings = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  @Schema(description = "Role of the user", example = "USER")
  private UserRole role;

  public enum UserRole {
    USER,
    BARBER,
    MANAGER,
  }
}
