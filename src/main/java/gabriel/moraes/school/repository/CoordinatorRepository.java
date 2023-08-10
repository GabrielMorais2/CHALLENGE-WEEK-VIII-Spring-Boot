package gabriel.moraes.school.repository;

import gabriel.moraes.school.Model.employee.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {

}