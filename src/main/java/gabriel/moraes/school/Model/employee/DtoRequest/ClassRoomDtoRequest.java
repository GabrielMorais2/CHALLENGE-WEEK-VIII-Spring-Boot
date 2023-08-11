package gabriel.moraes.school.Model.employee.DtoRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 1, message = "It is possible to register only 1 coordinator per class.")
    private List<Long> coordinators = new ArrayList<>();

    @NotNull
    @Size(max = 3, message = "It is possible to register only 1 scrumMaster per class.")
    private List<Long> scrumMasters = new ArrayList<>();

    @NotNull
    @Size(max = 3, message = "It is possible to register only 1 instructors per class.")
    private List<Long> instructors = new ArrayList<>();
}
