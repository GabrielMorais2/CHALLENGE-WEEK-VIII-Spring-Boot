package gabriel.moraes.school.Model.employee.DtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDtoResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

}
