package bsuir.coursework.HairSalon.repositories;

import bsuir.coursework.HairSalon.models.ResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ResetCodeRepository extends JpaRepository<ResetCode, Integer> {
  ResetCode findByUserEmailAndCode(String email, String resetCode);
}
