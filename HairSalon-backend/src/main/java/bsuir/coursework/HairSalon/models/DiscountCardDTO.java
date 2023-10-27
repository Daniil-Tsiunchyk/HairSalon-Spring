package bsuir.coursework.HairSalon.models;

import lombok.Data;

@Data
public class DiscountCardDTO {

  private int id;
  private Double discountPercentage;
  private String clientName;
  private Integer userId;

  public DiscountCardDTO(DiscountCard discountCard) {
    this.id = discountCard.getId();
    this.discountPercentage = discountCard.getDiscountPercentage();
    this.clientName =
      discountCard.getUser().getFirstName() +
      " " +
      discountCard.getUser().getLastName();
    this.userId = discountCard.getUser().getId();
  }
}
