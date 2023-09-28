package bsuir.coursework.HairSalon.repositories;

import bsuir.coursework.HairSalon.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUser_Id(int userId);
}
