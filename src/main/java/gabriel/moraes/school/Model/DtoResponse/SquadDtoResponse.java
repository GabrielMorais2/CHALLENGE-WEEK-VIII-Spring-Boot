package gabriel.moraes.school.Model.DtoResponse;

import gabriel.moraes.school.Model.Student;
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
public class SquadDtoResponse {

    private Long id;
    private String name;
    private List<Student> students = new ArrayList<>();

}
