package gabriel.moraes.school.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class ClassRoom {

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

    @OneToMany(mappedBy = "classRoom", fetch = FetchType.LAZY)
    private List<Instructor> instructors = new ArrayList<>();

    @OneToMany(mappedBy = "classRoom", fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "classRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Squad> squads = new ArrayList<>();

    public ClassRoom(String name, Coordinator coordinator, ScrumMaster scrumMaster, List<Instructor> instructors) {
        this.name = name;
        this.status = ClassStatus.WAITING;
        this.coordinator = coordinator;
        this.scrumMaster = scrumMaster;
        this.instructors = instructors;
    }

}
