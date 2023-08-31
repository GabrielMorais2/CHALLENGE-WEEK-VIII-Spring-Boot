package gabriel.moraes.school.domain.instructor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDtoResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

}
