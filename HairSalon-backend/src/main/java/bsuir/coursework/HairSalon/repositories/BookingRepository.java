package bsuir.coursework.HairSalon.repositories;

import bsuir.coursework.HairSalon.models.Booking;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BookingRepository extends JpaRepository<Booking, Integer> {
  List<Booking> findByUser_Id(int userId);

  List<Booking> findByDateTimeBeforeAndStatus(
    Date dateTime,
    Booking.ServiceStatus status
  );

  List<Booking> findByDateTimeAfterAndStatus(
    Date dateTime,
    Booking.ServiceStatus status
  );
}
