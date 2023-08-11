package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.ClassRoom;
import gabriel.moraes.school.Model.ClassStatus;
import gabriel.moraes.school.Model.Squad;
import gabriel.moraes.school.Model.Student;
import gabriel.moraes.school.Model.employee.DtoResponse.SquadDtoResponse;
import gabriel.moraes.school.exception.InvalidClassStatusException;
import gabriel.moraes.school.exception.NoRegisteredStudents;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import gabriel.moraes.school.repository.ClassRoomRepository;
import gabriel.moraes.school.repository.SquadRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SquadService {

    private final ClassRoomRepository classRoomRepository;
    private final SquadRepository squadRepository;
    private final ModelMapper mapper;

    @Autowired
    public SquadService(ClassRoomRepository classRoomRepository, SquadRepository squadRepository, ModelMapper mapper) {
        this.classRoomRepository = classRoomRepository;
        this.squadRepository = squadRepository;
        this.mapper = mapper;
    }

    @Transactional
    public List<SquadDtoResponse> createSquad(Long classId) {
        ClassRoom classRoom = getClassRoomById(classId);
        List<Squad> squads = createSquadsFromClassRoom(classRoom);
        updateClassRoomWithSquads(classRoom, squads);
        setSquadForStudents(squads);
        return mapSquadsToDtoResponse(squads);
    }

    private ClassRoom getClassRoomById(Long classId) {
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new ObjectNotFoundException("Class room not found with id: " + classId));

        if (classRoom.getStudents().isEmpty()) {
            throw new NoRegisteredStudents("There are no registered students.");
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
    public SquadDtoResponse updateSquadName(Long classId, Long squadId, String newName) {
        ClassRoom classRoom = getClassRoomById(classId);

        Squad squadToUpdate = classRoom.getSquads().stream()
                .filter(squad -> squad.getId().equals(squadId))
                .findFirst()
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
