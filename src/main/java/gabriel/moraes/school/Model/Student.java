package gabriel.moraes.school.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    @JsonBackReference
    private ClassRoom classRoom;

    @ManyToOne
    @JoinColumn(name = "squad_id")
    @JsonBackReference
    private Squad squad;


}
