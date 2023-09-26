package bsuir.coursework.HairSalon.repositories;

import bsuir.coursework.HairSalon.models.HairService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HairServiceRepository extends JpaRepository<HairService, Integer> {
}
