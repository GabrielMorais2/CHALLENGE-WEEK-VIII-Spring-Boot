package gabriel.moraes.school.Controller;

import gabriel.moraes.school.Model.employee.DtoRequest.InstructorDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.InstructorDtoResponse;
import gabriel.moraes.school.Service.InstructorService;
import gabriel.moraes.school.repository.InstructorRepository;
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

class InstructorControllerTest {
    public static final Long ID = 1L;
    public static final String FIRSTNAME = "Gabriel";
    public static final String LASTNAME = "Moraes";
    public static final String EMAIL = "gabriel@moraes";
    public static final String PHONE = "81984458436";
    @Mock
    private InstructorRepository instructorRepository;
    @InjectMocks
    private InstructorController instructorController;
    @Mock
    private InstructorService instructorService;
    private InstructorDtoRequest instructorDtoRequest;
    private InstructorDtoResponse instructorDtoResponse;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    @Test
    void WhenInstructorByIdThenReturnSuccess() {
        when(instructorService.getInstructorById(anyLong())).thenReturn(instructorDtoResponse);

        ResponseEntity<InstructorDtoResponse> response = instructorController.getInstructorById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(InstructorDtoResponse.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(FIRSTNAME, response.getBody().getFirstName());
        assertEquals(LASTNAME, response.getBody().getLastName());
        assertEquals(PHONE, response.getBody().getPhone());
        assertEquals(EMAIL, response.getBody().getEmail());

    }

    @Test
    void WhenGetAllInstructorsThenReturnAListOfInstructorDtoResponse() {
        when(instructorService.getAllInstructors()).thenReturn(List.of(instructorDtoResponse));

        ResponseEntity<List<InstructorDtoResponse>> response = instructorController.getAllInstructors();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(InstructorDtoResponse.class, response.getBody().get(0).getClass());

        assertEquals(ID, response.getBody().get(0).getId());
        assertEquals(FIRSTNAME, response.getBody().get(0).getFirstName());
        assertEquals(LASTNAME, response.getBody().get(0).getLastName());
        assertEquals(PHONE, response.getBody().get(0).getPhone());
        assertEquals(EMAIL, response.getBody().get(0).getEmail());

    }

    @Test
    void WhenSaveThenReturnAnInstructorDtoResponseCreated() {
        when(instructorService.save(any())).thenReturn(instructorDtoResponse);

        ResponseEntity<InstructorDtoResponse> response = instructorController.save(instructorDtoRequest);

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
        instructorDtoRequest = new InstructorDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
        instructorDtoResponse =  new InstructorDtoResponse(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }

}