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
    private String name;
    private String email;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    @JsonBackReference
    private ClassRoom classRoom;

    @ManyToOne
    @JoinColumn(name = "squad_id")
    @JsonBackReference
    private Squad squad;


}
