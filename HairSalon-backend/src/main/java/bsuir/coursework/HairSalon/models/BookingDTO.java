package bsuir.coursework.HairSalon.models;

import java.util.Date;
import lombok.Data;

@Data
public class BookingDTO {

  private Integer id;
  private Integer clientId;
  private String client;
  private String hairServiceName;
  private Double hairServiceCost;
  private Integer barberId;
  private String barber;
  private Date dateTime;
  private Booking.ServiceStatus status;

  public BookingDTO(Booking booking) {
    this.id = booking.getId();
    this.clientId = booking.getUser().getId();
    this.client =
      booking.getUser().getFirstName() + booking.getUser().getLastName();
    this.hairServiceName = booking.getHairService().getName();
    this.hairServiceCost = booking.getHairService().getCost();
    this.barberId = booking.getUser().getId();
    this.barber =
      booking.getUser().getFirstName() + booking.getUser().getLastName();
    this.dateTime = booking.getDateTime();
    this.status = booking.getStatus();
  }
}
