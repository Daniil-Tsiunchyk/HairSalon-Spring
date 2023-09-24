package bsuir.coursework.HairSalon.services;

import bsuir.coursework.HairSalon.models.Booking;
import bsuir.coursework.HairSalon.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            booking.setHairService(updatedBooking.getHairService());
            booking.setDateTime(updatedBooking.getDateTime());
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
}
