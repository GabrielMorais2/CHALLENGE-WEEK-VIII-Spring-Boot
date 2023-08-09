package gabriel.moraes.school.Model.employee.DtoRequest;

import gabriel.moraes.school.Model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddStudentsDtoRequest {
    List<Long> students;
}
