package bsuir.coursework.HairSalon.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "services")
@Schema(description = "Entity representing a hair salon service")
public class HairService {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "service_id")
  @Schema(
    description = "Unique identifier for the service",
    example = "1",
    hidden = true
  )
  private int id;

  @Column(nullable = false)
  @Schema(description = "Name of the hair service", example = "Haircut")
  private String name;

  @Column(nullable = false)
  @Schema(description = "Cost of the hair service", example = "25.0")
  private Double cost;

  @ElementCollection
  @CollectionTable(
    name = "establishment_images",
    joinColumns = @JoinColumn(name = "establishment_id")
  )
  @Column(name = "image_url")
  private List<String> images;
}
