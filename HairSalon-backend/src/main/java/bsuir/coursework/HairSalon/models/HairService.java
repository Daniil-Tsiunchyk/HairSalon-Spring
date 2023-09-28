package bsuir.coursework.HairSalon.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "services")
public class HairService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    int id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Double cost;
}
