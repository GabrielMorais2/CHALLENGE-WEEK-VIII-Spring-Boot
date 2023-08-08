package gabriel.moraes.school.Model.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gabriel.moraes.school.Model.ClassRoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instructor{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    @JsonIgnore
    private ClassRoom classRoom;
}
