package bsuir.coursework.HairSalon.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@Table(name = "users")
@Schema(description = "Entity representing a user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Schema(description = "Unique identifier for the user", example = "1", hidden = true)
    private int id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Password of the user", example = "password123")
    private String password;

    @Column()
    @Schema(description = "First name of the user", example = "Daniil")
    private String firstName;

    @Column()
    @Schema(description = "Last name of the user", example = "Makarov")
    private String lastName;

    @OneToMany(mappedBy = "user")
    @Schema(description = "List of bookings associated with the user", example = "[1, 2, 3]")
    private List<Booking> bookings;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Schema(description = "Role of the user", example = "USER")
    private UserRole role;

    public enum UserRole {
        USER,
        EMPLOYEE,
        MANAGER
    }
}
