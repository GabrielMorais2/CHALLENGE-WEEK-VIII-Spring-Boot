package gabriel.moraes.school.Model.employee;

import gabriel.moraes.school.Model.ClassPb;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class ScrumMaster extends Employee {

    @OneToOne(mappedBy = "scrumMaster")
    private ClassPb scrumMasterCourse;
}
