package bsuir.coursework.HairSalon.services;

import bsuir.coursework.HairSalon.models.DiscountCard;
import bsuir.coursework.HairSalon.models.DiscountCardDTO;
import bsuir.coursework.HairSalon.repositories.DiscountCardRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountCardService {

  private final DiscountCardRepository discountCardRepository;

  @Autowired
  public DiscountCardService(DiscountCardRepository discountCardRepository) {
    this.discountCardRepository = discountCardRepository;
  }

  public List<DiscountCardDTO> getAllDiscountCards() {
    List<DiscountCard> discountCards = discountCardRepository.findAll();
    return discountCards
      .stream()
      .map(DiscountCardDTO::new)
      .collect(Collectors.toList());
  }

  public Optional<DiscountCard> getDiscountCardById(int id) {
    return discountCardRepository.findById(id);
  }

  public DiscountCard createDiscountCard(DiscountCard discountCard) {
    return discountCardRepository.save(discountCard);
  }

  public DiscountCard updateDiscountCard(
    int id,
    DiscountCard updatedDiscountCard
  ) {
    Optional<DiscountCard> existingDiscountCard = discountCardRepository.findById(
      id
    );
    if (existingDiscountCard.isPresent()) {
      DiscountCard discountCard = existingDiscountCard.get();
      discountCard.setDiscountPercentage(
        updatedDiscountCard.getDiscountPercentage()
      );
      discountCard.setUser(updatedDiscountCard.getUser());
      return discountCardRepository.save(discountCard);
    } else {
      return null;
    }
  }

  public void deleteDiscountCard(int id) {
    discountCardRepository.deleteById(id);
  }

  public DiscountCardDTO getDiscountCardByUserId(int userId) {
    DiscountCard discountCard = discountCardRepository.findByUser_Id(userId);
    return discountCard != null ? new DiscountCardDTO(discountCard) : null;
  }
}
