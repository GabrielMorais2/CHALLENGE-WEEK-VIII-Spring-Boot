package gabriel.moraes.school.Controller;

import gabriel.moraes.school.Model.employee.DtoRequest.ScrumMasterDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.ScrumMasterDtoResponse;
import gabriel.moraes.school.Service.ScrumMasterService;
import gabriel.moraes.school.repository.ScrumMasterRepository;
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

class ScrumMasterControllerTest {

    public static final Long ID = 1L;
    public static final String FIRSTNAME = "Gabriel";
    public static final String LASTNAME = "Moraes";
    public static final String EMAIL = "gabriel@moraes";
    public static final String PHONE = "81984458436";
    @Mock
    private ScrumMasterRepository scrumMasterRepository;
    @InjectMocks
    private ScrumMasterController scrumMasterController;
    @Mock
    private ScrumMasterService scrumMasterService;
    private ScrumMasterDtoRequest scrumMasterDtoRequest;
    private ScrumMasterDtoResponse scrumMasterDtoResponse;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    @Test
    void WhenScrumMasterByIdThenReturnSuccess() {
        when(scrumMasterService.getScrumMasterById(anyLong())).thenReturn(scrumMasterDtoResponse);

        ResponseEntity<ScrumMasterDtoResponse> response = scrumMasterController.getScrumMasterById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ScrumMasterDtoResponse.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(FIRSTNAME, response.getBody().getFirstName());
        assertEquals(LASTNAME, response.getBody().getLastName());
        assertEquals(PHONE, response.getBody().getPhone());
        assertEquals(EMAIL, response.getBody().getEmail());

    }

    @Test
    void WhenGetAllScrumMastersThenReturnAListOfInstructorDtoResponse() {
        when(scrumMasterService.getAllScrumMasters()).thenReturn(List.of(scrumMasterDtoResponse));

        ResponseEntity<List<ScrumMasterDtoResponse>> response = scrumMasterController.getAllScrumMasters();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ScrumMasterDtoResponse.class, response.getBody().get(0).getClass());

        assertEquals(ID, response.getBody().get(0).getId());
        assertEquals(FIRSTNAME, response.getBody().get(0).getFirstName());
        assertEquals(LASTNAME, response.getBody().get(0).getLastName());
        assertEquals(PHONE, response.getBody().get(0).getPhone());
        assertEquals(EMAIL, response.getBody().get(0).getEmail());

    }

    @Test
    void WhenSaveThenReturnAnScrumMasterDtoResponseCreated() {
        when(scrumMasterService.save(any())).thenReturn(scrumMasterDtoResponse);

        ResponseEntity<ScrumMasterDtoResponse> response = scrumMasterController.save(scrumMasterDtoRequest);

        assertNotNull(response);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(ID, response.getBody().getId());
        assertEquals(FIRSTNAME, response.getBody().getFirstName());
        assertEquals(LASTNAME, response.getBody().getLastName());
        assertEquals(PHONE, response.getBody().getPhone());
        assertEquals(EMAIL, response.getBody().getEmail());

    }

    private void setupTestData(){
        scrumMasterDtoRequest = new ScrumMasterDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
        scrumMasterDtoResponse =  new ScrumMasterDtoResponse(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }

}