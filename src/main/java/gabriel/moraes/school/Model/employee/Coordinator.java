package gabriel.moraes.school.Model.employee;

import gabriel.moraes.school.Model.ClassPb;
import jakarta.persistence.*;

@Entity
public class Coordinator extends Employee {

    @OneToOne(mappedBy = "coordinator")
    private ClassPb coordinatorClass;
}
