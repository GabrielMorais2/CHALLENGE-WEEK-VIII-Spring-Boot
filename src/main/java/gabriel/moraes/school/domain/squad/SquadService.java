package gabriel.moraes.school.domain.squad;

import gabriel.moraes.school.domain.classroom.ClassRoom;
import gabriel.moraes.school.domain.classroom.ClassRoomService;
import gabriel.moraes.school.domain.classroom.ClassStatus;
import gabriel.moraes.school.domain.squad.dto.SquadDtoResponse;
import gabriel.moraes.school.domain.student.Student;
import gabriel.moraes.school.exception.InvalidClassStatusException;
import gabriel.moraes.school.exception.NoRegisteredStudentsException;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SquadService {

    private final ClassRoomService classRoomService;
    private final SquadRepository squadRepository;
    private final ModelMapper mapper;


    @Transactional
    public List<SquadDtoResponse> createSquad(Long classId) {
        ClassRoom classRoom = getClassRoomById(classId);
        List<Squad> squads = createSquadsFromClassRoom(classRoom);
        updateClassRoomWithSquads(classRoom, squads);
        setSquadForStudents(squads);
        return mapSquadsToDtoResponse(squads);
    }

    private ClassRoom getClassRoomById(Long classId) {
        ClassRoom classRoom = classRoomService.findClassById(classId);

        if (classRoom.getStudents().isEmpty()) {
            throw new NoRegisteredStudentsException("There are no registered students.");
        }

        if (classRoom.getStatus() != ClassStatus.STARTED) {
            throw new InvalidClassStatusException("It is only possible to create squads when a class is started.");
        }

        return classRoom;
    }

    private List<Squad> createSquadsFromClassRoom(ClassRoom classRoom) {
        List<Student> students = classRoom.getStudents();
        int maxStudentsPerSquad = 5;
        List<Squad> squads = new ArrayList<>();

        for (int studentIndex = 0; studentIndex < students.size(); studentIndex += maxStudentsPerSquad) {
            int currentSquadSize = Math.min(maxStudentsPerSquad, students.size() - studentIndex);
            List<Student> squadStudents = students.subList(studentIndex, studentIndex + currentSquadSize);
            Squad squad = new Squad("Uninformed", classRoom, squadStudents);
            squads.add(squad);
        }

        return squadRepository.saveAll(squads);
    }

    @Transactional
    public SquadDtoResponse updateSquadName(Long squadId, String newName) {
        Squad squadToUpdate = squadRepository.findById(squadId)
                .orElseThrow(() -> new ObjectNotFoundException("Squad not found with id: " + squadId));

        squadToUpdate.setName(newName);

        Squad updatedSquad = squadRepository.save(squadToUpdate);

        return mapper.map(updatedSquad, SquadDtoResponse.class);
    }

    private void updateClassRoomWithSquads(ClassRoom classRoom, List<Squad> squads) {
        classRoom.getSquads().addAll(squads);
    }

    private void setSquadForStudents(List<Squad> squads) {
        squads.forEach(squad -> squad.getStudents().forEach(student -> student.setSquad(squad)));
    }

    private List<SquadDtoResponse> mapSquadsToDtoResponse(List<Squad> squads) {
        return squads.stream()
                .map(squad -> mapper.map(squad, SquadDtoResponse.class))
                .collect(Collectors.toList());
    }
}
