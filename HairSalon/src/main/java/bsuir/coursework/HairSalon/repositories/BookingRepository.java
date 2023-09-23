package bsuir.coursework.HairSalon.repositories;

import bsuir.coursework.HairSalon.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
