package gabriel.moraes.school.Model.employee.DtoRequest;

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
