package gabriel.moraes.school.domain.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    private List<Long> coordinators = new ArrayList<>();

    @NotNull
    private List<Long> scrumMasters = new ArrayList<>();

    @NotNull
    private List<Long> instructors = new ArrayList<>();
}
