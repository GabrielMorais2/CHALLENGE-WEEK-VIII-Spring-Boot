package gabriel.moraes.school.Model.DtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatorDtoResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

}
