package gabriel.moraes.school.Controller;

import gabriel.moraes.school.Model.employee.DtoRequest.StudentDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.StudentDtoResponse;
import gabriel.moraes.school.Service.StudentService;
import gabriel.moraes.school.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class StudentControllerTest {

    public static final Long ID = 1L;
    public static final String FIRSTNAME = "Gabriel";
    public static final String LASTNAME = "Moraes";
    public static final String EMAIL = "gabriel@moraes";
    public static final String PHONE = "81984458436";
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentController studentController;
    @Mock
    private StudentService studentService;
    private StudentDtoRequest studentDtoRequest;
    private StudentDtoResponse studentDtoResponse;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    @Test
    void WhenStudentByIdThenReturnSuccess() {
        when(studentService.getStudentById(anyLong())).thenReturn(studentDtoResponse);

        ResponseEntity<StudentDtoResponse> response = studentController.getStudentById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StudentDtoResponse.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(FIRSTNAME, response.getBody().getFirstName());
        assertEquals(LASTNAME, response.getBody().getLastName());
        assertEquals(PHONE, response.getBody().getPhone());
        assertEquals(EMAIL, response.getBody().getEmail());

    }

    @Test
    void WhenGetAllStudentsThenReturnAListOfStudentDtoResponse() {
        when(studentService.getAllStudents()).thenReturn(List.of(studentDtoResponse));

        ResponseEntity<List<StudentDtoResponse>> response = studentController.getAllStudents();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StudentDtoResponse.class, response.getBody().get(0).getClass());

        assertEquals(ID, response.getBody().get(0).getId());
        assertEquals(FIRSTNAME, response.getBody().get(0).getFirstName());
        assertEquals(LASTNAME, response.getBody().get(0).getLastName());
        assertEquals(PHONE, response.getBody().get(0).getPhone());
        assertEquals(EMAIL, response.getBody().get(0).getEmail());

    }

    @Test
    void WhenSaveThenReturnAnStudentDtoResponseCreated() {
        when(studentService.save(any())).thenReturn(studentDtoResponse);

        ResponseEntity<StudentDtoResponse> response = studentController.save(studentDtoRequest);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(ID, response.getBody().getId());
        assertEquals(FIRSTNAME, response.getBody().getFirstName());
        assertEquals(LASTNAME, response.getBody().getLastName());
        assertEquals(PHONE, response.getBody().getPhone());
        assertEquals(EMAIL, response.getBody().getEmail());

    }

    private void setupTestData(){
        studentDtoRequest = new StudentDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
        studentDtoResponse =  new StudentDtoResponse(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }

}