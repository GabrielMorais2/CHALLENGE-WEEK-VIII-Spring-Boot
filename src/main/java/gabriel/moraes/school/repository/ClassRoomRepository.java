package gabriel.moraes.school.repository;

import gabriel.moraes.school.Model.ClassRoom;
import gabriel.moraes.school.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
}
