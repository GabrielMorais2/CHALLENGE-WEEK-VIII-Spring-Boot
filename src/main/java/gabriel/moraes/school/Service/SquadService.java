package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.ClassRoom;
import gabriel.moraes.school.Model.Squad;
import gabriel.moraes.school.Model.Student;
import gabriel.moraes.school.Model.employee.DtoRequest.SquadDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.ClassRoomDtoResponse;
import gabriel.moraes.school.Model.employee.DtoResponse.SquadDtoResponse;
import gabriel.moraes.school.repository.ClassRoomRepository;
import gabriel.moraes.school.repository.SquadRepository;
import gabriel.moraes.school.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SquadService {

    @Autowired
    private ClassRoomRepository classRoomRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SquadRepository squadRepository;
    @Autowired
    private ModelMapper mapper;
    @Transactional
    public List<SquadDtoResponse> createSquad(Long idClass) {

        ClassRoom classRoom = classRoomRepository.findById(idClass)
                .orElseThrow(() -> new EntityNotFoundException("Class room not found with id: " + idClass));

        List<Squad> squads = getSquads(classRoom);

        squads = squadRepository.saveAll(squads);

        classRoom.getSquads().addAll(squads);

        for (Squad squad : squads) {
            List<Student> squadStudents = squad.getStudents();
            squadStudents.forEach(student -> student.setSquad(squad));
        }

        return squads.stream()
                .map(squad -> mapper.map(squad, SquadDtoResponse.class))
                .collect(Collectors.toList());
    }

    private static List<Squad> getSquads(ClassRoom classRoom) {
        List<Student> students = classRoom.getStudents();

        int maxStudentsPerSquad = 5;

        int numSquads = (int) Math.ceil((double) students.size() / maxStudentsPerSquad);

        List<Squad> squads = new ArrayList<>();

        int studentIndex = 0;

        for (int i = 0; i < numSquads; i++) {

            int currentSquadSize = Math.min(maxStudentsPerSquad, students.size() - studentIndex);

            List<Student> squadStudents = students.subList(studentIndex, studentIndex + currentSquadSize);

            Squad squad = new Squad("Não informado", classRoom, squadStudents);

            squads.add(squad);

            studentIndex += currentSquadSize;
        }
        return squads;
    }

    private List<Student> findStudentsByIds(List<Long> studentIds) {
        return studentRepository.findAllById(studentIds);
    }
}
