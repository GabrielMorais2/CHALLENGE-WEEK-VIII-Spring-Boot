package gabriel.moraes.school.service;

import gabriel.moraes.school.domain.student.*;
import gabriel.moraes.school.domain.student.dto.StudentDtoRequest;
import gabriel.moraes.school.domain.student.dto.StudentDtoResponse;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    @Spy
    private ModelMapper mapper;

    @BeforeEach
    public void setup() {
        setupTestData();
    }
    @Test
    public void getStudentById_ReturnAnStudentDtoResponse() {
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
    public void getStudentsById_WithInvalidId_ReturnAnObjectNotFoundException() {
        Mockito.when(studentRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> studentService.getStudentById(anyLong()));
    }

    @Test
    void getAllStudents_ReturnAnListOfStudentsDtoResponse() {
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
    void saveStudent_ReturnAnStudentDtoResponse() {
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

        assertThrows(ObjectNotFoundException.class, () -> studentService.deleteStudentById(anyLong()));
    }


    private void setupTestData(){
        student = new Student(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
        studentDtoRequest = new StudentDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }

}