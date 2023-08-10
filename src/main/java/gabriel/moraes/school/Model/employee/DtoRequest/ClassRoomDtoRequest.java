package gabriel.moraes.school.Model.employee.DtoRequest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomDtoRequest {

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^(?!\\s)[\\p{L}\\d]+(?:[\\s-][\\p{L}\\d]+)*$", message = "Name should only contain letters, numbers, and spaces")
    private String name;

    @NotNull
    @Max(value = 1, message = "It is possible to register only 1 coordinator per class.")
    private Long coordinator;

    @NotNull
    @Max(value = 1, message = "It is possible to register only 1 coordinator per class.")
    private Long scrumMaster;

    @NotNull
    @Size(max = 3)
    private List<Long> instructors = new ArrayList<>();
}
