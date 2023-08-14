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

    public Student(Long id, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public Student(Long studentId) {
        this.id = studentId;
    }
}
