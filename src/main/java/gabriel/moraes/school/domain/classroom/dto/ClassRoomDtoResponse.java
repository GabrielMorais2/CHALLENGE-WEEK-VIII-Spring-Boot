package gabriel.moraes.school.domain.classroom.dto;

import gabriel.moraes.school.domain.classroom.ClassStatus;
import gabriel.moraes.school.domain.coordinator.Coordinator;
import gabriel.moraes.school.domain.instructor.Instructor;
import gabriel.moraes.school.domain.scrummaster.ScrumMaster;
import gabriel.moraes.school.domain.squad.Squad;
import gabriel.moraes.school.domain.student.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomDtoResponse {

    private Long id;
    private String name;
    private ClassStatus status;
    private List<Coordinator> coordinators = new ArrayList<>();
    private List<ScrumMaster> scrumMasters = new ArrayList<>();
    private List<Instructor> instructors = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
    private List<Squad> squads = new ArrayList<>();

    public ClassRoomDtoResponse(Long id, String name, ClassStatus status, List<Coordinator> coordinators, List<ScrumMaster> scrumMasters, List<Instructor> instructors) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.coordinators = coordinators;
        this.scrumMasters = scrumMasters;
        this.instructors = instructors;
    }

    public ClassRoomDtoResponse(Long id, String name, ClassStatus status, List<Coordinator> coordinators, List<ScrumMaster> scrumMasters, List<Instructor> instructors, List<Student> students) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.coordinators = coordinators;
        this.scrumMasters = scrumMasters;
        this.instructors = instructors;
        this.students = students;
    }
}
