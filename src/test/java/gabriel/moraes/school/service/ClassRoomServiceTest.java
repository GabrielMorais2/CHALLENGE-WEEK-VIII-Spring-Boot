package gabriel.moraes.school.service;

import gabriel.moraes.school.Model.*;
import gabriel.moraes.school.Model.DtoRequest.AddStudentsDtoRequest;
import gabriel.moraes.school.Model.DtoRequest.ClassRoomDtoRequest;
import gabriel.moraes.school.Model.DtoResponse.ClassRoomDtoResponse;
import gabriel.moraes.school.Utils.JsonUtils;
import gabriel.moraes.school.exception.*;
import gabriel.moraes.school.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassRoomServiceTest {

    public static final Long ID = 1L;
    @Mock
    private ClassRoomRepository classRoomRepository;
    @InjectMocks
    private ClassRoomService classRoomService;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private CoordinatorRepository coordinatorRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ScrumMasterRepository scrumMasterRepository;
    @Spy
    private ModelMapper mapper;


    private static final String CLASSROOM = "Payload/ClassRoom/ClassRoom_Created.json";
    private static final String CLASSROOM_WITH_STUDENTS = "Payload/ClassRoom/CLASSROOM_WITH_STUDENTS_AND_START.json";
    private static final String CLASSROOM_WITH_STUDENTS_MAXIMUM = "Payload/ClassRoom/CLASSROOM_WITH_STUDENTS_MAXIMUM.json";
    private static final String CLASSROOM_DTO_REQUEST = "Payload/ClassRoom/ClassRoomDtoRequest.json";
    private static final String COORDINATOR = "Payload/Coordinator/Coordinator.json";
    private static final String INSTRUCTOR = "Payload/Instructor/Instructor.json";
    private static final String SCRUM_MASTER = "Payload/ScrumMaster/ScrumMaster.json";
    private static final String STUDENTS = "Payload/STUDENTS/STUDENTS.json";
    private static final String ADDSTUDENTSDTOREQUEST = "Payload/STUDENTS/AddStudentsDtoRequest.json";


    @Test
    public void createClass_ReturnSuccess() throws IOException {

        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM, ClassRoom.class);
        ClassRoomDtoRequest classRoomDtoRequest = JsonUtils.getObjectFromFile(CLASSROOM_DTO_REQUEST, ClassRoomDtoRequest.class);
        Coordinator[] coordinators = JsonUtils.getObjectFromFile(COORDINATOR, Coordinator[].class);
        Instructor[] instructors = JsonUtils.getObjectFromFile(INSTRUCTOR, Instructor[].class);
        ScrumMaster[] scrumMasters = JsonUtils.getObjectFromFile(SCRUM_MASTER, ScrumMaster[].class);


        when(coordinatorRepository.findAllById(any())).thenReturn(List.of(coordinators));
        when(scrumMasterRepository.findAllById(any())).thenReturn(List.of(scrumMasters));
        when(instructorRepository.findAllById((any()))).thenReturn(List.of(instructors));
        when(classRoomRepository.save(any(ClassRoom.class))).thenReturn(classRoom);

        ClassRoomDtoResponse response = classRoomService.createClass(classRoomDtoRequest);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals("The Fofoca Brokers", response.getName());
        assertEquals(ClassStatus.WAITING, response.getStatus());
        assertEquals(Arrays.stream(coordinators).toList().get(0), response.getCoordinators().get(0));
        assertEquals(Arrays.stream(scrumMasters).toList().get(0), response.getScrumMasters().get(0));
        assertEquals(Arrays.stream(instructors).toList().get(0), response.getInstructors().get(0));
    }
    @Test
    public void createClass_WithNotFoundCoordinator_ReturnObjectNotFoundException() throws IOException {
        ClassRoomDtoRequest classRoomDtoRequest = JsonUtils.getObjectFromFile(CLASSROOM_DTO_REQUEST, ClassRoomDtoRequest.class);

        when(coordinatorRepository.findAllById(any())).thenReturn(List.of());

        assertThrows(ObjectNotFoundException.class, () -> classRoomService.createClass(classRoomDtoRequest));
    }

    @Test
    public void createClass_WithNotFoundScrumMaster_ReturnObjectNotFoundException() throws IOException {
        ClassRoomDtoRequest classRoomDtoRequest = JsonUtils.getObjectFromFile(CLASSROOM_DTO_REQUEST, ClassRoomDtoRequest.class);

        when(coordinatorRepository.findAllById(any())).thenReturn(List.of(new Coordinator()));
        when(scrumMasterRepository.findAllById(any())).thenReturn(List.of());

        assertThrows(ObjectNotFoundException.class, () -> classRoomService.createClass(classRoomDtoRequest));
    }

    @Test
    public void createClass_WithNotFoundInstructor_ReturnObjectNotFoundException() throws IOException {
        ClassRoomDtoRequest classRoomDtoRequest = JsonUtils.getObjectFromFile(CLASSROOM_DTO_REQUEST, ClassRoomDtoRequest.class);

        when(coordinatorRepository.findAllById(any())).thenReturn(List.of(new Coordinator()));
        when(scrumMasterRepository.findAllById(any())).thenReturn(List.of(new ScrumMaster()));
        when(instructorRepository.findAllById((any()))).thenReturn(List.of());

        assertThrows(ObjectNotFoundException.class, () -> classRoomService.createClass(classRoomDtoRequest));
    }

    @Test
    public void getClassById_ReturnSuccess() throws IOException {
        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM, ClassRoom.class);

        when(classRoomRepository.findById(anyLong())).thenReturn(Optional.of(classRoom));

        ClassRoomDtoResponse response = classRoomService.getClassById(ID);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals("The Fofoca Brokers", response.getName());
        assertEquals(ClassStatus.WAITING, response.getStatus());
    }
    @Test
    public void getClassById_ReturnNotFound(){
        when(classRoomRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> classRoomService.getClassById(ID));
    }

    @Test
    public void finishClass_ReturnSuccess() throws IOException {
        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM, ClassRoom.class);

        classRoom.setStatus(ClassStatus.STARTED);

        when(classRoomRepository.findById(ID)).thenReturn(Optional.of(classRoom));

        assertDoesNotThrow(() -> classRoomService.finish(ID));
        assertEquals(ClassStatus.FINISHED, classRoom.getStatus());
    }

    @Test
    public void finishClass_WithFinishedStatus_ReturnInvalidClassStatusException() throws IOException {
        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM, ClassRoom.class);

        classRoom.setStatus(ClassStatus.FINISHED);

        when(classRoomRepository.findById(anyLong())).thenReturn(Optional.of(classRoom));

        assertThrows(InvalidClassStatusException.class, () -> classRoomService.finish(ID));
    }

    @Test
    void finishClass_WithWaitingStatus_ReturnInvalidClassStatusException() throws IOException {
        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM, ClassRoom.class);

        classRoom.setStatus(ClassStatus.WAITING);

        when(classRoomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(classRoom));

        assertThrows(InvalidClassStatusException.class, () -> classRoomService.finish(ID));
    }

    @Test
    public void startClass_WithWaitingStatusAndSufficientStudents_ReturnSuccess() throws IOException {
        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM_WITH_STUDENTS, ClassRoom.class);

        classRoom.setStatus(ClassStatus.WAITING);

        when(classRoomRepository.findById(ID)).thenReturn(Optional.of(classRoom));

        assertDoesNotThrow(() -> classRoomService.startClass(ID));

        verify(classRoomRepository, times(1)).findById(ID);
    }

    @Test
    public void startClass_WithWaitingStatusAndInsufficientStudents_ReturnInsufficientStudentsException() throws IOException {
        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM_WITH_STUDENTS, ClassRoom.class);

        classRoom.setStatus(ClassStatus.WAITING);
        classRoom.setStudents(new ArrayList<>());

        when(classRoomRepository.findById(ID)).thenReturn(Optional.of(classRoom));

        assertThrows(InsufficientStudentsException.class, () -> classRoomService.startClass(ID));
        assertEquals(ClassStatus.WAITING, classRoom.getStatus());
    }

    @Test
    public void startClass_WithOtherStatus_ReturnInvalidClassStatusException() throws IOException {
        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM_WITH_STUDENTS, ClassRoom.class);

        classRoom.setStatus(ClassStatus.FINISHED);

        when(classRoomRepository.findById(ID)).thenReturn(Optional.of(classRoom));

        assertThrows(InvalidClassStatusException.class, () -> classRoomService.startClass(ID));
        assertEquals(ClassStatus.FINISHED, classRoom.getStatus());

    }

    @Test
    public void addStudentsToClass_Success() throws IOException {

        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM_WITH_STUDENTS, ClassRoom.class);

        classRoom.setStudents(new ArrayList<>());
        classRoom.setStatus(ClassStatus.WAITING);

        Student[] students = JsonUtils.getObjectFromFile(STUDENTS, Student[].class);
        AddStudentsDtoRequest studentIds = JsonUtils.getObjectFromFile(ADDSTUDENTSDTOREQUEST, AddStudentsDtoRequest.class);

        when(classRoomRepository.findById(ID)).thenReturn(Optional.of(classRoom));
        when(studentRepository.findAllById(studentIds.getStudents())).thenReturn(List.of(students));
        when(classRoomRepository.save(classRoom)).thenReturn(classRoom);

        ClassRoomDtoResponse response = classRoomService.addStudentsToClass(ID, studentIds);

        assertNotNull(response);

        assertEquals(studentIds.getStudents().size(), response.getStudents().size());

        verify(classRoomRepository, times(1)).save(classRoom);
    }

    @Test
    public void addStudentsToClass_InvalidClassStatus() throws IOException {
        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM_WITH_STUDENTS, ClassRoom.class);

        Student[] students = JsonUtils.getObjectFromFile(STUDENTS, Student[].class);
        AddStudentsDtoRequest studentIds = JsonUtils.getObjectFromFile(ADDSTUDENTSDTOREQUEST, AddStudentsDtoRequest.class);

        when(classRoomRepository.findById(ID)).thenReturn(Optional.of(classRoom));
        when(studentRepository.findAllById(studentIds.getStudents())).thenReturn(List.of(students));

        assertThrows(InvalidClassStatusException.class, () -> classRoomService.addStudentsToClass(ID, studentIds));
    }

    @Test
    public void addStudentsToClass_WithMaximumStudents_ReturnMaximumStudentsException() throws IOException {

        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM_WITH_STUDENTS_MAXIMUM, ClassRoom.class);
        classRoom.setStatus(ClassStatus.WAITING);

        Student[] students = JsonUtils.getObjectFromFile(STUDENTS, Student[].class);
        AddStudentsDtoRequest studentIds = JsonUtils.getObjectFromFile(ADDSTUDENTSDTOREQUEST, AddStudentsDtoRequest.class);

        when(classRoomRepository.findById(ID)).thenReturn(Optional.of(classRoom));
        when(studentRepository.findAllById(studentIds.getStudents())).thenReturn(List.of(students));

        assertThrows(MaximumStudentsException.class, () -> classRoomService.addStudentsToClass(ID, studentIds));

    }

    @Test
    public void addStudentsToClass_WithInvalidStudents_ReturnStudentAlreadyAssignedException() throws IOException {
        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM_WITH_STUDENTS, ClassRoom.class);
        classRoom.setStatus(ClassStatus.WAITING);

        Student[] students = JsonUtils.getObjectFromFile(STUDENTS, Student[].class);
        Arrays.stream(students).toList().get(0).setClassRoom(classRoom);

        AddStudentsDtoRequest studentIds = JsonUtils.getObjectFromFile(ADDSTUDENTSDTOREQUEST, AddStudentsDtoRequest.class);

        when(classRoomRepository.findById(ID)).thenReturn(Optional.of(classRoom));
        when(studentRepository.findAllById(studentIds.getStudents())).thenReturn(List.of(students));

        assertThrows(StudentAlreadyAssignedException.class, () -> classRoomService.addStudentsToClass(ID, studentIds));
    }
}

