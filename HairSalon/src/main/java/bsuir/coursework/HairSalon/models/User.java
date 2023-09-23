package bsuir.coursework.HairSalon.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @Column()
    String firstName;

    @Column()
    String lastName;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    public enum UserRole {
        USER,
        EMPLOYEE,
        MANAGER
    }
}
