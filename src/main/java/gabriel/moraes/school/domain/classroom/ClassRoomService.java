package gabriel.moraes.school.domain.classroom;

import gabriel.moraes.school.domain.classroom.dto.AddStudentsDtoRequest;
import gabriel.moraes.school.domain.classroom.dto.ClassRoomDtoRequest;
import gabriel.moraes.school.domain.classroom.dto.ClassRoomDtoResponse;
import gabriel.moraes.school.domain.coordinator.Coordinator;
import gabriel.moraes.school.domain.coordinator.CoordinatorService;
import gabriel.moraes.school.domain.instructor.Instructor;
import gabriel.moraes.school.domain.instructor.InstructorService;
import gabriel.moraes.school.domain.scrummaster.ScrumMaster;
import gabriel.moraes.school.domain.scrummaster.ScrumMasterService;
import gabriel.moraes.school.domain.student.Student;
import gabriel.moraes.school.domain.student.StudentService;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static gabriel.moraes.school.validations.ValidationsClassRoom.*;

@Service
@AllArgsConstructor
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;

    private final InstructorService instructorService;
    private final CoordinatorService coordinatorService;
    private final StudentService studentService;
    private final ScrumMasterService scrumMasterService;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public ClassRoomDtoResponse getClassById(Long id) {
        ClassRoom classRoom = findClassById(id);
        return mapper.map(classRoom, ClassRoomDtoResponse.class);
    }

    @Transactional
    public ClassRoomDtoResponse createClass(ClassRoomDtoRequest classDto) {

        List<Coordinator> coordinators = coordinatorService.getAllCoordinatorsById(classDto.getCoordinators());
        List<ScrumMaster> scrumMasters = scrumMasterService.getAllScrumMastersById(classDto.getScrumMasters());
        List<Instructor> instructors = instructorService.getAllInstructorsById(classDto.getInstructors());

        validateMaxCoordinator(coordinators);
        validateMaxInstructor(instructors);
        validateMaxScrumMaster(scrumMasters);

        ClassRoom classRoom = new ClassRoom(classDto.getName());

        classRoom.getCoordinators().addAll(coordinators);
        classRoom.getScrumMasters().addAll(scrumMasters);
        classRoom.getInstructors().addAll(instructors);

        ClassRoom savedClassRoom = classRoomRepository.save(classRoom);

        return mapper.map(savedClassRoom, ClassRoomDtoResponse.class);
    }

    @Transactional
    public ClassRoomDtoResponse addStudentsToClass(Long id, AddStudentsDtoRequest addStudentsDtoRequest) {
        ClassRoom classRoom = findClassById(id);

        List<Student> students = studentService.getAllStudentsById(addStudentsDtoRequest.getStudents());

        validateClassRoomStatus(classRoom.getStatus());
        validateAndAssignStudentsToClass(students, classRoom);
        validationsMaxStudents(classRoom.getStudents());

        classRoom.getStudents().addAll(students);

        classRoomRepository.save(classRoom);

        return mapper.map(classRoom, ClassRoomDtoResponse.class);
    }

    @Transactional
    public void finish(Long id) {
        ClassRoom classRoom = findClassById(id);
        if (validateFinishClass(classRoom)) {
            classRoom.setStatus(ClassStatus.FINISHED);
        }
    }

    @Transactional
    public void startClass(Long id) {
        ClassRoom classRoom = findClassById(id);
        if (validateStartClass(classRoom)) {
            classRoom.setStatus(ClassStatus.STARTED);
        }
    }

    public ClassRoom findClassById(Long id) {
        return classRoomRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Class room not found with id: " + id));
    }
}