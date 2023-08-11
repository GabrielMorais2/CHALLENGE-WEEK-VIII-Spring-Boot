package gabriel.moraes.school.Model.employee.DtoRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrumMasterDtoRequest {

    @NotBlank(message = "firstName is required")
    @Pattern(regexp = "^(?!\\s)[\\p{L}\\d]+(?:[\\s-][\\p{L}\\d]+)*$", message = "Name should only contain letters, numbers, and spaces")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Pattern(regexp = "^(?!\\s)[\\p{L}\\d]+(?:[\\s-][\\p{L}\\d]+)*$", message = "Name should only contain letters, numbers, and spaces")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "phone is required")
    @Pattern(regexp = "^\\+?[0-9\\-\\s]+$", message = "Invalid phone number format")
    private String phone;


}
