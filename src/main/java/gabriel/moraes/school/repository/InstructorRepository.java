package gabriel.moraes.school.repository;

import gabriel.moraes.school.Model.employee.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
