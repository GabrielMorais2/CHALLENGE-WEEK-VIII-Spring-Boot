package gabriel.moraes.school.repository;

import gabriel.moraes.school.Model.employee.ScrumMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrumMasterRepository extends JpaRepository<ScrumMaster, Long> {
}
