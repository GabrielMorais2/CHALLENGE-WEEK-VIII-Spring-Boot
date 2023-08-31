package gabriel.moraes.school.domain.classroom.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddStudentsDtoRequest {
    @Size(max = 30, message = "A class can have a maximum of 30 students")
    List<Long> students;
}
