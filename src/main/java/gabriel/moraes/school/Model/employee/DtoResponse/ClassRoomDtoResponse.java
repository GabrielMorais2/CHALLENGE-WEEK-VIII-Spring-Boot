package gabriel.moraes.school.Model.employee.DtoResponse;

import gabriel.moraes.school.Model.ClassStatus;
import gabriel.moraes.school.Model.Student;
import gabriel.moraes.school.Model.employee.Coordinator;
import gabriel.moraes.school.Model.employee.Instructor;
import gabriel.moraes.school.Model.employee.ScrumMaster;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomDtoResponse {

    private Long id;
    private String name;
    private ClassStatus status;
    private Coordinator coordinator;
    private ScrumMaster scrumMaster;
    private List<Instructor> instructors = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

}
