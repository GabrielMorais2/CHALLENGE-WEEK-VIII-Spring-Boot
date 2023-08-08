package gabriel.moraes.school.Model.employee.DtoRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomDtoRequest {

    @NotBlank
    private String name;
    @NotNull
    private Long coordinator;
    @NotNull
    private Long scrumMaster;
    @NotNull
    private List<Long> instructors = new ArrayList<>();
    @NotNull
    private List<Long> students = new ArrayList<>();
}
