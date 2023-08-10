package gabriel.moraes.school.repository;

import gabriel.moraes.school.Model.Squad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SquadRepository extends JpaRepository<Squad, Long> {
}
