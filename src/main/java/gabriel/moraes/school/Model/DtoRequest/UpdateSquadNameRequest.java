package gabriel.moraes.school.Model.DtoRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSquadNameRequest {

    @NotBlank
    private String name;

}
