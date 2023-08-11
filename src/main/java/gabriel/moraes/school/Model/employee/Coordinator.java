package gabriel.moraes.school.Model.employee;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gabriel.moraes.school.Model.ClassRoom;
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
public class Coordinator{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @ManyToMany(mappedBy = "coordinators")
    @JsonBackReference
    private List<ClassRoom> classRooms = new ArrayList<>();

    public Coordinator(Long id, String firstname, String lastname, String email, String phone) {
        this.id = id;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
        this.phone = phone;
    }
}
