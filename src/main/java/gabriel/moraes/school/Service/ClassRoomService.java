package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.ClassRoom;
import gabriel.moraes.school.Model.ClassStatus;
import gabriel.moraes.school.Model.Student;
import gabriel.moraes.school.Model.employee.Coordinator;
import gabriel.moraes.school.Model.employee.DtoRequest.AddStudentsDtoRequest;
import gabriel.moraes.school.Model.employee.DtoRequest.ClassRoomDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.ClassRoomDtoResponse;
import gabriel.moraes.school.Model.employee.Instructor;
import gabriel.moraes.school.Model.employee.ScrumMaster;
import gabriel.moraes.school.exception.InvalidClassStatusException;
import gabriel.moraes.school.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassRoomService {

    private int minStudent = 15;
    private int maxStudent = 30;
    private int maxInstructors = 3;

    private final InstructorRepository instructorRepository;
    private final CoordinatorRepository coordinatorRepository;
    private final StudentRepository studentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final ScrumMasterRepository scrumMasterRepository;
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

    @Transactional(readOnly = true)
    public ClassRoomDtoResponse getClassById(Long id) {
        ClassRoom classRoom = findClassById(id);
        return mapper.map(classRoom, ClassRoomDtoResponse.class);
    }

    @Transactional
    public ClassRoomDtoResponse createClass(ClassRoomDtoRequest classDto) {
        Coordinator coordinator = findCoordinatorById(classDto.getCoordinator());
        ScrumMaster scrumMaster = findScrumMasterById(classDto.getScrumMaster());
        List<Instructor> instructors = findInstructorsByIds(classDto.getInstructors());

        validateInstructors(instructors);

        ClassRoom classRoom = new ClassRoom(classDto.getName(), coordinator, scrumMaster, instructors);
        assignClassToInstructors(instructors, classRoom);

        ClassRoom savedClassRoom = classRoomRepository.save(classRoom);

        return mapper.map(savedClassRoom, ClassRoomDtoResponse.class);
    }

    public ClassRoomDtoResponse addStudentsToClass(Long id, AddStudentsDtoRequest addStudentsDtoRequest) {
        ClassRoom classRoom = findClassById(id);
        List<Student> students = findStudentsByIds(addStudentsDtoRequest.getStudents());

        validateStudents(students);
        assignClassToStudents(students, classRoom);

        classRoom.getStudents().addAll(students);
        classRoomRepository.save(classRoom);

        return mapper.map(classRoom, ClassRoomDtoResponse.class);
    }

    private ClassRoom findClassById(Long id) {
        return classRoomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Class room not found with id: " + id));
    }

    private Coordinator findCoordinatorById(Long coordinatorId) {
        return coordinatorRepository.findById(coordinatorId)
                .orElseThrow(() -> new EntityNotFoundException("Coordinator not found"));
    }

    private ScrumMaster findScrumMasterById(Long scrumMasterId) {
        return scrumMasterRepository .findById(scrumMasterId)
                .orElseThrow(() -> new EntityNotFoundException("Scrum Master not found"));
    }

    private List<Instructor> findInstructorsByIds(List<Long> instructorIds) {
        return instructorRepository.findAllById(instructorIds);
    }

    private List<Student> findStudentsByIds(List<Long> studentIds) {
        return studentRepository.findAllById(studentIds);
    }

    private void validateStartStatus(ClassRoom classRoom) {
        int studentsCount = classRoom.getStudents().size();
        if (studentsCount < minStudent || studentsCount > maxStudent) {
            throw new IllegalArgumentException("A minimum of 15 students is required to start a class.");
        }

        if (classRoom.getStatus() != ClassStatus.WAITING) {
            throw new InvalidClassStatusException("To start a class you need the status in WAITING");
        }

        classRoom.setStatus(ClassStatus.STARTED);
    }

    private void validateInstructors(List<Instructor> instructors) {
        if (instructors.size() < maxInstructors) {
            throw new IllegalArgumentException("Requires a minimum of 3 instructors");
        }
    }

    private void validateStudents(List<Student> students) {
        int studentsCount = students.size();
        if (studentsCount > maxStudent) {
            throw new IllegalArgumentException("A class can have a maximum of 30 students");
        }
    }

    private void assignClassToStudents(List<Student> students, ClassRoom classRoom) {
        if (classRoom.getStatus() != ClassStatus.WAITING) {
            throw new InvalidClassStatusException("It is only possible to add new students when the class room status is in WAITING");
        }

        for (Student student : students) {
            if (student.getClassRoom() != null) {
                throw new IllegalArgumentException("Student " + student.getFirstName() + "[ID: "+ student.getId()+"]"  + " is already assigned to a class.");
            }
            student.setClassRoom(classRoom);
        }
    }

    private void assignClassToInstructors(List<Instructor> instructors, ClassRoom classRoom) {
        for (Instructor instructor : instructors) {
            if (instructor.getClassRoom() != null) {
                throw new IllegalArgumentException("Instructor " + instructor.getFirstName() + " is already assigned to a class.");
            }
            instructor.setClassRoom(classRoom);
        }
    }

    @Transactional
    public void finish(Long id) {
        ClassRoom classRoom = findClassById(id);

        if (classRoom.getStatus() == ClassStatus.STARTED) {
            classRoom.setStatus(ClassStatus.FINISHED);
        } else if (classRoom.getStatus() == ClassStatus.FINISHED) {
            throw new InvalidClassStatusException("Class room is already finished.");
        } else {
            throw new InvalidClassStatusException("Classroom needs to be in STARTED status to be finished.");
        }
    }

    @Transactional
    public void startClass(Long id) {
        ClassRoom classRoom = findClassById(id);
        validateStartStatus(classRoom);
    }
}