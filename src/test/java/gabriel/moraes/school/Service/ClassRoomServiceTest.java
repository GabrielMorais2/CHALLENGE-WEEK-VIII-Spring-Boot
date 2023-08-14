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
import gabriel.moraes.school.exception.InsufficientStudentsException;
import gabriel.moraes.school.exception.InvalidClassStatusException;
import gabriel.moraes.school.exception.MaximumStudentsException;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import gabriel.moraes.school.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassRoomServiceTest {

    public static final Long ID = 1L;
    @Mock
    private ClassRoomRepository classRoomRepository;
    @InjectMocks
    private ClassRoomService classRoomService;
    private ClassRoomDtoRequest classRoomDtoRequest;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private CoordinatorRepository coordinatorRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ScrumMasterRepository scrumMasterRepository;
    private ClassRoom classRoom;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        ModelMapper mapper = new ModelMapper();
        classRoomDtoRequest = new ClassRoomDtoRequest("the fofoca brokers", List.of(1L), List.of(1L), List.of(1L));
        classRoom = new ClassRoom(ID, "the fofoca brokers", ClassStatus.WAITING, List.of(new Coordinator()), List.of(new ScrumMaster()), List.of(new Instructor()), List.of(new Student()));
        classRoomService = new ClassRoomService(
                instructorRepository,
                mapper,
                scrumMasterRepository,
                studentRepository,
                coordinatorRepository,
                classRoomRepository
        );
    }

    @Test
    public void createClass_ReturnSuccess() {
        List<Long> coordinatorIds = List.of(1L, 2L);
        List<Long> scrumMasterIds = List.of(1L);
        List<Long> instructorIds = List.of(1L, 2L, 3L);

        classRoomDtoRequest.setCoordinators(coordinatorIds);
        classRoomDtoRequest.setScrumMasters(scrumMasterIds);
        classRoomDtoRequest.setInstructors(instructorIds);

        List<Coordinator> coordinators = List.of(new Coordinator(), new Coordinator());
        List<ScrumMaster> scrumMasters = List.of(new ScrumMaster());
        List<Instructor> instructors = List.of(new Instructor(), new Instructor(), new Instructor());

        when(coordinatorRepository.findAllById(coordinatorIds)).thenReturn(coordinators);
        when(scrumMasterRepository.findAllById(scrumMasterIds)).thenReturn(scrumMasters);
        when(instructorRepository.findAllById(instructorIds)).thenReturn(instructors);

        ClassRoom savedClassRoom = new ClassRoom(ID, "the fofoca brokers", ClassStatus.WAITING, coordinators, scrumMasters, instructors, new ArrayList<>());
        when(classRoomRepository.save(any(ClassRoom.class))).thenReturn(savedClassRoom);

        ClassRoomDtoResponse response = classRoomService.createClass(classRoomDtoRequest);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals("the fofoca brokers", response.getName());
        assertEquals(ClassStatus.WAITING, response.getStatus());
        assertEquals(coordinators.size(), response.getCoordinators().size());
        assertEquals(scrumMasters.size(), response.getScrumMasters().size());
        assertEquals(instructors.size(), response.getInstructors().size());
    }


    @Test
    public void createClass_WithNotFoundCoordinator_ReturnObjectNotFoundException() {
        List<Long> coordinatorIds = List.of(1L);

        classRoomDtoRequest.setCoordinators(coordinatorIds);

        when(coordinatorRepository.findAllById(coordinatorIds)).thenThrow(new ObjectNotFoundException("Coordinators not found for IDs: ["+ coordinatorIds.get(0) +"]"));

        try {
            classRoomService.createClass(classRoomDtoRequest);
        } catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals("Coordinators not found for IDs: [" + coordinatorIds.get(0) +"]", ex.getMessage());
        }
    }



    @Test
    public void getClassById_ReturnSuccess() {
        when(classRoomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(classRoom));

        ClassRoomDtoResponse response = classRoomService.getClassById(ID);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals("the fofoca brokers", response.getName());
        assertEquals(ClassStatus.WAITING, response.getStatus());
    }

    @Test
    public void getClassById_ReturnNotFound() {
        when(classRoomRepository.findById(Mockito.anyLong())).thenThrow(new ObjectNotFoundException("Class not found with id:" + ID));

        try {
            classRoomService.getClassById(ID);
        } catch (Exception ex){
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals("Class not found with id:" + ID, ex.getMessage());
        }
    }

    @Test
    public void finishClass_ReturnSuccess() {
        Long classId = 1L;
        classRoom.setStatus(ClassStatus.STARTED);

        when(classRoomRepository.findById(classId)).thenReturn(Optional.of(classRoom));

        assertDoesNotThrow(() -> classRoomService.finish(classId));

        assertEquals(ClassStatus.FINISHED, classRoom.getStatus());
    }

    @Test
    public void finishClass_WithFinishedStatus_ReturnInvalidClassStatusException() {
        Long classId = 1L;
        classRoom.setStatus(ClassStatus.FINISHED);

        when(classRoomRepository.findById(Mockito.anyLong())).thenThrow(new InvalidClassStatusException("Class room is already finished"));

        try {
            classRoomService.finish(classId);
        } catch (Exception ex) {
            assertEquals(InvalidClassStatusException.class, ex.getClass());
            assertEquals("Class room is already finished", ex.getMessage());
        }
    }

    @Test
    void finishClass_WithWaitingStatus_ReturnInvalidClassStatusException() {
        Long classId = 1L;
        classRoom.setStatus(ClassStatus.WAITING);
        when(classRoomRepository.findById(Mockito.anyLong())).thenThrow(new InvalidClassStatusException("Classroom needs to be in STARTED status to be finished."));

        try {
            classRoomService.finish(classId);
        } catch (Exception ex) {
            assertEquals(InvalidClassStatusException.class, ex.getClass());
            assertEquals("Classroom needs to be in STARTED status to be finished.", ex.getMessage());
        }

    }

    @Test
    public void startClass_WithWaitingStatusAndSufficientStudents_ReturnSuccess() {
        Long classId = 1L;
        classRoom.setStatus(ClassStatus.WAITING);
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            students.add(new Student());
        }

        classRoom.setStudents(students);

        when(classRoomRepository.findById(classId)).thenReturn(Optional.of(classRoom));

        assertDoesNotThrow(() -> classRoomService.startClass(classId));

        assertEquals(ClassStatus.STARTED, classRoom.getStatus());
        assertEquals(classRoom.getStudents().size(), students.size());
    }

    @Test
    public void startClass_WithWaitingStatusAndInsufficientStudents_ReturnInsufficientStudentsException() {
        Long classId = 1L;
        classRoom.setStatus(ClassStatus.WAITING);

        List<Student> students = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            students.add(new Student());
        }
        classRoom.setStudents(students);

        when(classRoomRepository.findById(classId)).thenThrow(new InsufficientStudentsException("A minimum of 15 students is required to start a class."));

        try {
            classRoomService.startClass(classId);
        } catch (Exception ex) {
            assertEquals(InsufficientStudentsException.class, ex.getClass());
            assertEquals("A minimum of 15 students is required to start a class.", ex.getMessage());
            assertEquals(ClassStatus.WAITING, classRoom.getStatus());
        }
    }

    @Test
    public void startClass_WithOtherStatus_ReturnInvalidClassStatusException() {
        Long classId = 1L;
        classRoom.setStatus(ClassStatus.FINISHED);

        when(classRoomRepository.findById(classId)).thenThrow(new InvalidClassStatusException("To start a class you need the status in WAITING"));

        try {
            classRoomService.startClass(classId);
        } catch (Exception ex) {
            assertEquals(InvalidClassStatusException.class, ex.getClass());
            assertEquals("To start a class you need the status in WAITING", ex.getMessage());
        }
    }

    @Test
    public void addStudentsToClass_Success() {
        Long classId = 1L;
        List<Long> studentIds = List.of(1L, 2L, 3L);

        List<Student> students = new ArrayList<>();

        for (Long studentId : studentIds) {
            students.add(new Student(studentId));
        }

        ClassRoom classRoom = new ClassRoom(ID, "the fofoca brokers", ClassStatus.WAITING, List.of(new Coordinator()), List.of(new ScrumMaster()), List.of(new Instructor()), new ArrayList<>());

        when(classRoomRepository.findById(classId)).thenReturn(Optional.of(classRoom));
        when(studentRepository.findAllById(studentIds)).thenReturn(students);

        AddStudentsDtoRequest addStudentsDtoRequest = new AddStudentsDtoRequest();
        addStudentsDtoRequest.setStudents(studentIds);

        ClassRoomDtoResponse response = classRoomService.addStudentsToClass(classId, addStudentsDtoRequest);

        assertEquals(studentIds.size(), response.getStudents().size());
        verify(classRoomRepository, times(1)).save(classRoom);
    }

    @Test
    public void addStudentsToClass_InvalidClassStatus() {
        Long classId = 1L;
        List<Long> studentIds = List.of(1L, 2L, 3L);

        ClassRoom classRoom = new ClassRoom(ID, "the fofoca brokers", ClassStatus.STARTED, List.of(new Coordinator()), List.of(new ScrumMaster()), List.of(new Instructor()), List.of());
        when(classRoomRepository.findById(classId)).thenThrow(new InvalidClassStatusException("It is only possible to add new students when the class room status is in WAITING"));

        AddStudentsDtoRequest addStudentsDtoRequest = new AddStudentsDtoRequest();
        addStudentsDtoRequest.setStudents(studentIds);

        try {
            classRoomService.addStudentsToClass(classId, addStudentsDtoRequest);
        } catch (Exception ex) {
            assertEquals(InvalidClassStatusException.class, ex.getClass());
            assertEquals("It is only possible to add new students when the class room status is in WAITING", ex.getMessage());
        }
    }

    @Test
    public void addStudentsToClass_InvalidStudents() {
        Long classId = 1L;
        List<Long> studentIds = List.of(1L, 2L, 3L);

        ClassRoom classRoom = new ClassRoom(ID, "the fofoca brokers", ClassStatus.STARTED, List.of(new Coordinator()), List.of(new ScrumMaster()), List.of(new Instructor()), List.of());
        when(classRoomRepository.findById(classId)).thenThrow(new MaximumStudentsException("A class can have a maximum of 30 students"));

        AddStudentsDtoRequest addStudentsDtoRequest = new AddStudentsDtoRequest();
        addStudentsDtoRequest.setStudents(studentIds);

        try {
            classRoomService.addStudentsToClass(classId, addStudentsDtoRequest);
        } catch (Exception ex) {
            assertEquals(MaximumStudentsException.class, ex.getClass());
            assertEquals("A class can have a maximum of 30 students", ex.getMessage());
        }
    }
}

