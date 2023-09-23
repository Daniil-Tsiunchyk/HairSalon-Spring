package bsuir.coursework.HairSalon.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    int id;

    @Column(unique = true, nullable = false)
    String name;

    @Column(nullable = false)
    Double cost;
}
