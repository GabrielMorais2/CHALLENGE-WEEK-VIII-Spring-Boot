package gabriel.moraes.school.Model.employee;

import gabriel.moraes.school.Model.ClassPb;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Instructor extends Employee {

    @ManyToOne
    @JoinColumn(name = "class_pb_id")
    private ClassPb instructorClass;
}
