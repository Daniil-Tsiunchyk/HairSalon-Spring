package bsuir.coursework.HairSalon.repositories;

import bsuir.coursework.HairSalon.models.Booking;
import bsuir.coursework.HairSalon.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUser_Id(int userId);
}
