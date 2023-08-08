package gabriel.moraes.school.repository;

import gabriel.moraes.school.Model.Student;
import gabriel.moraes.school.Model.employee.ScrumMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
