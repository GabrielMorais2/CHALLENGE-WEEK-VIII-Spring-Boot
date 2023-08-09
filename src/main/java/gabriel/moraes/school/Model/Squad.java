package gabriel.moraes.school.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Squad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    @JsonManagedReference
    private ClassRoom classRoom;

    @OneToMany(mappedBy = "squad", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Student> students = new ArrayList<>();

    public Squad(String name, ClassRoom classRoom, List<Student> squadStudents) {
        this.name = name;
        this.classRoom = classRoom;
        this.students = squadStudents;
    }
}
