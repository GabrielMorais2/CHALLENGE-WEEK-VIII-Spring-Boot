package gabriel.moraes.school.Model;

import gabriel.moraes.school.Model.employee.Coordinator;
import gabriel.moraes.school.Model.employee.Instructor;
import gabriel.moraes.school.Model.employee.ScrumMaster;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassPb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private ClassStatus status;

    @OneToOne
    @JoinColumn(name = "coordinator_id")
    private Coordinator coordinator;

    @OneToOne
    @JoinColumn(name = "scrum_master_id")
    private ScrumMaster scrumMaster;

    @OneToMany(mappedBy = "instructorClass")
    private List<Instructor> instructors = new ArrayList<>();

    @OneToMany(mappedBy = "studentClass")
    private List<Student> students = new ArrayList<>();

}
