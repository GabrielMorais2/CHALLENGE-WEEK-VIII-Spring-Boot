package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.Student;
import gabriel.moraes.school.Model.employee.DtoRequest.StudentDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.StudentDtoResponse;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import gabriel.moraes.school.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class StudentServiceTest {

    public static final Long ID = 1L;
    public static final String FIRSTNAME = "Gabriel";
    public static final String LASTNAME = "Moraes";
    public static final String EMAIL = "gabriel@moraes";
    public static final String PHONE = "81984458436";

    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;
    private Student student;
    private StudentDtoRequest studentDtoRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ModelMapper mapper = new ModelMapper();
        studentService = new StudentService(studentRepository, mapper);
        setupTestData();
    }
    @Test
    public void whenGetStudentByIdThenReturnAnStudentDtoResponse() {
        Mockito.when(studentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(student));

        StudentDtoResponse response = studentService.getStudentById(ID);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals(FIRSTNAME, response.getFirstName());
        assertEquals(LASTNAME, response.getLastName());
        assertEquals(PHONE, response.getPhone());
        assertEquals(EMAIL, response.getEmail());
    }

    @Test
    public void whenGetStudentByIdThenReturnAnObjectNotFoundException() {
        Mockito.when(studentRepository.findById(Mockito.anyLong())).thenThrow(new ObjectNotFoundException("student not found with id:" + ID));

        try {
            studentService.getStudentById(ID);
        } catch (Exception ex){
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals("student not found with id:" + ID, ex.getMessage());
        }
    }

    @Test
    void whenGetAllStudentsThenReturnAnListOfStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<StudentDtoResponse> response = studentService.getAllStudents();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(ID, response.get(0).getId());
        assertEquals(FIRSTNAME, response.get(0).getFirstName());
        assertEquals(LASTNAME, response.get(0).getLastName());
        assertEquals(PHONE, response.get(0).getPhone());
        assertEquals(EMAIL, response.get(0).getEmail());
    }

    @Test
    void WhenSaveStudentThenReturnAnStudentDtoResponse() {
        when(studentRepository.save(any())).thenReturn(student);

        StudentDtoResponse response = studentService.save(studentDtoRequest);

        assertNotNull(response);
        assertEquals(StudentDtoResponse.class, response.getClass());
        assertEquals(FIRSTNAME, response.getFirstName());
        assertEquals(LASTNAME, response.getLastName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PHONE, response.getPhone());

    }

    @Test
    void deleteStudent_WithExistingId_doesNotThrowAnyException(){
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        assertThatCode(() -> studentService.deleteStudentById(1L)).doesNotThrowAnyException();
    }

    @Test
    void deleteStudent_WithUnexistingId_ReturnNotFound(){
        when(studentRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> studentService.deleteStudentById(99L)).isInstanceOf(ObjectNotFoundException.class);
    }


    private void setupTestData(){
        student = new Student(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
        studentDtoRequest = new StudentDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }

}