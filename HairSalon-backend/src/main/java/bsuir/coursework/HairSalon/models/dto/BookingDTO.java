package bsuir.coursework.HairSalon.models.dto;

import java.time.LocalDateTime;

import bsuir.coursework.HairSalon.models.Booking;
import lombok.Data;

@Data
public class BookingDTO {

  private Integer id;
  private Integer clientId;
  private String client;
  private Integer serviceId;
  private String hairServiceName;
  private Double hairServiceCost;
  private Integer barberId;
  private String barber;
  private LocalDateTime dateTime;
  private Booking.ServiceStatus status;

  public BookingDTO(Booking booking) {
    this.id = booking.getId();
    this.clientId = booking.getUser().getId();
    this.client =
      booking.getUser().getFirstName() + " " + booking.getUser().getLastName();
    this.serviceId = booking.getHairService().getId();
    this.hairServiceName = booking.getHairService().getName();
    this.hairServiceCost = booking.getHairService().getCost();
    this.barberId = booking.getBarber().getId();
    this.barber =
      booking.getBarber().getFirstName() + " " + booking.getBarber().getLastName();
    this.dateTime = booking.getDateTime();
    this.status = booking.getStatus();
  }
}
