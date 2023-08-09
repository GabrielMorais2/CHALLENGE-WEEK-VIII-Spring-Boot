package gabriel.moraes.school.Model.employee;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gabriel.moraes.school.Model.ClassRoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinator{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @OneToOne(mappedBy = "coordinator")
    @JsonBackReference
    private ClassRoom coordinatorClass;
}
