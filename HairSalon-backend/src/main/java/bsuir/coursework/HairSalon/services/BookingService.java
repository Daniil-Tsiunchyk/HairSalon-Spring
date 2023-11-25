package bsuir.coursework.HairSalon.services;

import bsuir.coursework.HairSalon.models.Booking;
import bsuir.coursework.HairSalon.repositories.BookingRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

  private final BookingRepository bookingRepository;

  @Autowired
  public BookingService(BookingRepository bookingRepository) {
    this.bookingRepository = bookingRepository;
  }

  public List<Booking> getAllBookings() {
    return bookingRepository.findAll();
  }

  public List<Booking> getPastBookings() {
    Date currentDate = new Date();
    return bookingRepository.findByDateTimeBeforeAndStatus(
      currentDate,
      Booking.ServiceStatus.RESERVED
    );
  }

  public List<Booking> getFutureBookings() {
    Date currentDate = new Date();
    return bookingRepository.findByDateTimeAfterAndStatus(
      currentDate,
      Booking.ServiceStatus.RESERVED
    );
  }

  public Optional<Booking> getBookingById(int id) {
    return bookingRepository.findById(id);
  }

  public Booking createBooking(Booking booking) {
    return bookingRepository.save(booking);
  }

  public Booking updateBooking(int id, Booking updatedBooking) {
    Optional<Booking> existingBooking = bookingRepository.findById(id);
    if (existingBooking.isPresent()) {
      Booking booking = existingBooking.get();
      booking.setUser(updatedBooking.getUser());
      booking.setBarber(updatedBooking.getBarber());
      booking.setHairService(updatedBooking.getHairService());
      booking.setDateTime(updatedBooking.getDateTime());
      booking.setLocation(updatedBooking.getLocation());
      booking.setStatus(updatedBooking.getStatus());
      return bookingRepository.save(booking);
    } else {
      return null;
    }
  }

  public void deleteBooking(int id) {
    bookingRepository.deleteById(id);
  }

  public List<Booking> getBookingsByUser(int userId) {
    return bookingRepository.findByUser_Id(userId);
  }

  public boolean isBarberAvailable(int barberId, LocalDateTime dateTime) {
    LocalDateTime startTime = dateTime.minusMinutes(5);
    LocalDateTime endTime = dateTime.plusMinutes(20);

    return !bookingRepository.existsByBarberIdAndDateTimeBetween(barberId, startTime, endTime);
  }

  public List<Booking> getBookingsForBarber(int barberId) {
    return bookingRepository.findByBarberId(barberId);
  }
}
