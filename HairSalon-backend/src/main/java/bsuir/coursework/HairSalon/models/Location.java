package bsuir.coursework.HairSalon.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "locations")
@Schema(description = "Entity representing a location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    @Schema(
            description = "Unique identifier for the location",
            example = "1",
            hidden = true
    )
    private int id;

    @Column(name = "latitude", nullable = false)
    @Schema(description = "Latitude coordinate of the location")
    private double latitude;

    @Column(name = "longitude", nullable = false)
    @Schema(description = "Longitude coordinate of the location")
    private double longitude;
}
