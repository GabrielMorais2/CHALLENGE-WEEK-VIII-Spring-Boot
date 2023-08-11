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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "classroom_coordinators",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "coordinator_id"))
    private List<Coordinator> coordinators = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "classroom_scrum_masters",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "scrum_master_id"))
    private List<ScrumMaster> scrumMasters = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "classroom_instructors",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id"))
    private List<Instructor> instructors = new ArrayList<>();

    @OneToMany(mappedBy = "classRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "classRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Squad> squads = new ArrayList<>();

    public ClassRoom(String name) {
        this.name = name;
        this.status = ClassStatus.WAITING;
    }

    public ClassRoom(Long id, String name, ClassStatus status, List<Coordinator> coordinators, List<ScrumMaster> scrumMasters, List<Instructor> instructors, List<Student> students) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.coordinators = coordinators;
        this.scrumMasters = scrumMasters;
        this.instructors = instructors;
        this.students = students;
    }
}
