package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.DiscountCard;
import bsuir.coursework.HairSalon.services.DiscountCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/discount-cards")
public class DiscountCardController {
    private final DiscountCardService discountCardService;

    @Autowired
    public DiscountCardController(DiscountCardService discountCardService) {
        this.discountCardService = discountCardService;
    }

    @GetMapping
    public ResponseEntity<List<DiscountCard>> getAllDiscountCards() {
        List<DiscountCard> discountCards = discountCardService.getAllDiscountCards();
        return ResponseEntity.ok(discountCards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountCard> getDiscountCardById(@PathVariable int id) {
        Optional<DiscountCard> discountCard = discountCardService.getDiscountCardById(id);
        return discountCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DiscountCard> createDiscountCard(@RequestBody DiscountCard discountCard) {
        DiscountCard createdDiscountCard = discountCardService.createDiscountCard(discountCard);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDiscountCard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountCard> updateDiscountCard(@PathVariable int id, @RequestBody DiscountCard updatedDiscountCard) {
        DiscountCard updated = discountCardService.updateDiscountCard(id, updatedDiscountCard);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscountCard(@PathVariable int id) {
        discountCardService.deleteDiscountCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DiscountCard> getDiscountCardByUserId(@PathVariable int userId) {
        DiscountCard discountCard = discountCardService.getDiscountCardByUserId(userId);
        if (discountCard != null) {
            return ResponseEntity.ok(discountCard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
