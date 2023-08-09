package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.ClassRoom;
import gabriel.moraes.school.Model.Student;
import gabriel.moraes.school.Model.employee.Coordinator;
import gabriel.moraes.school.Model.employee.DtoRequest.ClassRoomDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.ClassRoomDtoResponse;
import gabriel.moraes.school.Model.employee.Instructor;
import gabriel.moraes.school.Model.employee.ScrumMaster;
import gabriel.moraes.school.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassRoomService {

    private final InstructorRepository instructorRepository;
    private final CoordinatorRepository coordinatorRepository;
    private final StudentRepository studentRepository;
    private final ScrumMasterRepository scrumMasterRepository;
    private final ClassRoomRepository classRoomRepository;
    private final ModelMapper mapper;

    public ClassRoomService(InstructorRepository instructorRepository, ModelMapper mapper,
                            ScrumMasterRepository scrumMasterRepository, StudentRepository studentRepository,
                            CoordinatorRepository coordinatorRepository, ClassRoomRepository classRoomRepository) {
        this.instructorRepository = instructorRepository;
        this.scrumMasterRepository = scrumMasterRepository;
        this.studentRepository = studentRepository;
        this.coordinatorRepository = coordinatorRepository;
        this.classRoomRepository = classRoomRepository;
        this.mapper = mapper;
    }

    public ClassRoomDtoResponse createClass(ClassRoomDtoRequest classDto) {

        Coordinator coordinator = findCoordinatorById(classDto.getCoordinator());
        ScrumMaster scrumMaster = findScrumMasterById(classDto.getScrumMaster());
        List<Instructor> instructors = findInstructorsByIds(classDto.getInstructors());
        List<Student> students = findStudentsByIds(classDto.getStudents());

        validateInstructors(instructors);
        validateStudents(students);

        ClassRoom classRoom = new ClassRoom(classDto.getName(), coordinator, scrumMaster, instructors, students);

        assignClassToStudents(students, classRoom);
        assignClassToInstructors(instructors, classRoom);

        ClassRoom savedClassRoom = classRoomRepository.save(classRoom);

        return mapper.map(savedClassRoom, ClassRoomDtoResponse.class);
    }


    private Coordinator findCoordinatorById(Long coordinatorId) {
        return coordinatorRepository.findById(coordinatorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordinator not found"));
    }

    private ScrumMaster findScrumMasterById(Long scrumMasterId) {
        return scrumMasterRepository.findById(scrumMasterId)
                .orElseThrow(() -> new IllegalArgumentException("Scrum Master not found"));
    }

    private List<Instructor> findInstructorsByIds(List<Long> instructorIds) {
        return instructorRepository.findAllById(instructorIds);
    }

    private List<Student> findStudentsByIds(List<Long> studentIds) {
        return studentRepository.findAllById(studentIds);
    }

    private void validateInstructors(List<Instructor> instructors) {
        if (instructors.size() < 3) {
            throw new IllegalArgumentException("Requires a minimum of 3 instructors");
        }
    }

    private void validateStudents(List<Student> students) {
        int studentsCount = students.size();
        if (studentsCount < 5 || studentsCount > 30) {
            throw new IllegalArgumentException("Requires a minimum of 15 students and a maximum of 30");
        }
    }

    private void assignClassToStudents(List<Student> students, ClassRoom classRoom) {
        for (Student student : students) {
            if (student.getClassRoom() != null) {
                throw new IllegalArgumentException("Student " + student.getName() + " is already assigned to a class.");
            }
        }

        students.forEach(student -> student.setClassRoom(classRoom));
    }

    private void assignClassToInstructors(List<Instructor> instructors, ClassRoom classRoom) {
        for (Instructor instructor : instructors) {
            if (instructor.getClassRoom() != null) {
                throw new IllegalArgumentException("Student " + instructor.getName() + " is already assigned to a class.");
            }
        }

        instructors.forEach(instructor -> instructor.setClassRoom(classRoom));
    }

    public ClassRoomDtoResponse getClassById(Long id) {
        ClassRoom classRoom = classRoomRepository.findById(id).get();
        return mapper.map(classRoom, ClassRoomDtoResponse.class);
    }
}