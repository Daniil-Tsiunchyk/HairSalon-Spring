package bsuir.coursework.HairSalon.repositories;

import bsuir.coursework.HairSalon.models.DiscountCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountCardRepository extends JpaRepository<DiscountCard, Integer> {
}
