package gabriel.moraes.school.repository;

import gabriel.moraes.school.Model.employee.Coordinator;
import gabriel.moraes.school.Model.employee.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {

}