package bsuir.coursework.HairSalon.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "discount_cards")
public class DiscountCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_card_id")
    private int id;

    @Column(nullable = false)
    Double discountPercentage;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
