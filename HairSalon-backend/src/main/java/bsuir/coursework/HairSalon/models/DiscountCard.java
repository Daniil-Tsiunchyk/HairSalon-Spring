package bsuir.coursework.HairSalon.models;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@Table(name = "discount_cards")
@Schema(description = "Entity representing a discount card for users")
public class DiscountCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_card_id")
    @Schema(description = "Unique identifier for the discount card", example = "1", hidden = true)
    private int id;

    @Column(nullable = false)
    @Schema(description = "Discount percentage associated with the card", example = "10.0")
    private Double discountPercentage;

    @OneToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "User associated with the discount card")
    private User user;
}
