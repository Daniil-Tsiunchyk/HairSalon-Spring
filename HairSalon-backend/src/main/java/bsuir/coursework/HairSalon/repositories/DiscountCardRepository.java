package bsuir.coursework.HairSalon.repositories;

import bsuir.coursework.HairSalon.models.DiscountCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DiscountCardRepository
  extends JpaRepository<DiscountCard, Integer> {
  DiscountCard findByUser_Id(int userId);
}
