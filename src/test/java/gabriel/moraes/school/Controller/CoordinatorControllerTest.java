package gabriel.moraes.school.Controller;

import gabriel.moraes.school.Model.employee.DtoRequest.CoordinatorDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.CoordinatorDtoResponse;
import gabriel.moraes.school.Service.CoordinatorService;
import gabriel.moraes.school.repository.CoordinatorRepository;
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

class CoordinatorControllerTest {

    public static final Long ID = 1L;
    public static final String FIRSTNAME = "Gabriel";
    public static final String LASTNAME = "Moraes";
    public static final String EMAIL = "gabriel@moraes";
    public static final String PHONE = "81984458436";
    @Mock
    private CoordinatorRepository coordinatorRepository;
    @InjectMocks
    private CoordinatorController coordinatorController;
    @Mock
    private CoordinatorService coordinatorService;
    private CoordinatorDtoRequest coordinatorDtoRequest;
    private CoordinatorDtoResponse coordinatorDtoResponse;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    @Test
    void WhenCoordinatorByIdThenReturnSuccess() {
        when(coordinatorService.getCoordinatorById(anyLong())).thenReturn(coordinatorDtoResponse);

        ResponseEntity<CoordinatorDtoResponse> response = coordinatorController.getCoordinatorById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(CoordinatorDtoResponse.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(FIRSTNAME, response.getBody().getFirstName());
        assertEquals(LASTNAME, response.getBody().getLastName());
        assertEquals(PHONE, response.getBody().getPhone());
        assertEquals(EMAIL, response.getBody().getEmail());

    }

    @Test
    void WhenGetAllCoordinatorThenReturnAListOfCoordinatorDtoResponse() {
        when(coordinatorService.getAllCoordinators()).thenReturn(List.of(coordinatorDtoResponse));

        ResponseEntity<List<CoordinatorDtoResponse>> response = coordinatorController.getAllCoordinator();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(CoordinatorDtoResponse.class, response.getBody().get(0).getClass());

        assertEquals(ID, response.getBody().get(0).getId());
        assertEquals(FIRSTNAME, response.getBody().get(0).getFirstName());
        assertEquals(LASTNAME, response.getBody().get(0).getLastName());
        assertEquals(PHONE, response.getBody().get(0).getPhone());
        assertEquals(EMAIL, response.getBody().get(0).getEmail());

    }

    @Test
    void WhenSaveThenReturnAnCoordinatorDtoResponseCreated() {
        when(coordinatorService.save(any())).thenReturn(coordinatorDtoResponse);

        ResponseEntity<CoordinatorDtoResponse> response = coordinatorController.save(coordinatorDtoRequest);

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
        coordinatorDtoRequest = new CoordinatorDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
        coordinatorDtoResponse =  new CoordinatorDtoResponse(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }
}