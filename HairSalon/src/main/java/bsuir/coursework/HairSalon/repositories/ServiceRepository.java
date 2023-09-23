package bsuir.coursework.HairSalon.repositories;

import bsuir.coursework.HairSalon.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
}
