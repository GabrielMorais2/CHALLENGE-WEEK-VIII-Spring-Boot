package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.employee.DtoRequest.InstructorDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.InstructorDtoResponse;
import gabriel.moraes.school.Model.employee.Instructor;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import gabriel.moraes.school.repository.InstructorRepository;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    public static final Long ID = 1L;
    public static final String FIRSTNAME = "Gabriel";
    public static final String LASTNAME = "Moraes";
    public static final String EMAIL = "gabriel@moraes";
    public static final String PHONE = "81984458436";

    @Mock
    private InstructorRepository instructorRepository;
    @InjectMocks
    private InstructorService instructorService;

    private Instructor instructor;
    private InstructorDtoRequest instructorDtoRequest;
    @Spy
    private ModelMapper mapper;

    @BeforeEach
    public void setup() {
        setupTestData();
    }
    @Test
    public void getInstructorById_ReturnAnInstructorDtoResponse() {
        when(instructorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(instructor));

        InstructorDtoResponse response = instructorService.getInstructorById(ID);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals(FIRSTNAME, response.getFirstName());
        assertEquals(LASTNAME, response.getLastName());
        assertEquals(PHONE, response.getPhone());
        assertEquals(EMAIL, response.getEmail());
    }

    @Test
    public void getInstructorById_WithInvalidId_ReturnAnObjectNotFoundException() {
        when(instructorRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> instructorService.getInstructorById(anyLong()));
    }

    @Test
    void getAllInstructor_ReturnAnListOfInstructorDtoResponse() {
        when(instructorRepository.findAll()).thenReturn(List.of(instructor));

        List<InstructorDtoResponse> response = instructorService.getAllInstructors();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(ID, response.get(0).getId());
        assertEquals(FIRSTNAME, response.get(0).getFirstName());
        assertEquals(LASTNAME, response.get(0).getLastName());
        assertEquals(PHONE, response.get(0).getPhone());
        assertEquals(EMAIL, response.get(0).getEmail());

    }

    @Test
    void saveInstructor_ReturnAnInstructorDtoResponse() {
        when(instructorRepository.save(any())).thenReturn(instructor);

        InstructorDtoResponse response = instructorService.save(instructorDtoRequest);

        assertNotNull(response);
        assertEquals(InstructorDtoResponse.class, response.getClass());
        assertEquals(FIRSTNAME, response.getFirstName());
        assertEquals(LASTNAME, response.getLastName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PHONE, response.getPhone());

    }

    private void setupTestData(){
        instructor = new Instructor(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
        instructorDtoRequest = new InstructorDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }

}