package gabriel.moraes.school.repository;

import gabriel.moraes.school.Model.Squad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SquadRepository extends JpaRepository<Squad, Long> {
    List<Squad> findByClassRoomId(Long classRoomId);
}
