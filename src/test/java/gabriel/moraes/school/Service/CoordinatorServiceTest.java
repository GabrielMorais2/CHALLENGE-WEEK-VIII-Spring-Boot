package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.employee.Coordinator;
import gabriel.moraes.school.Model.employee.DtoRequest.CoordinatorDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.CoordinatorDtoResponse;
import gabriel.moraes.school.repository.CoordinatorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CoordinatorServiceTest {
    public static final Long ID = 1L;
    public static final String FIRSTNAME = "Gabriel";
    public static final String LASTNAME = "Moraes";
    public static final String EMAIL = "gabriel@moraes";
    public static final String PHONE = "81984458436";

    @Mock
    private CoordinatorRepository coordinatorRepository;
    @InjectMocks
    private CoordinatorService coordinatorService;

    private Coordinator coordinator;
    private CoordinatorDtoRequest coordinatorDtoRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ModelMapper mapper = new ModelMapper();
        coordinatorService = new CoordinatorService(coordinatorRepository, mapper);
        startUser();
    }
    @Test
    public void whenGetCoordinatorByIdThenReturnAnCoordinator() {
        Mockito.when(coordinatorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(coordinator));

        CoordinatorDtoResponse response = coordinatorService.getCoordinatorById(ID);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals(FIRSTNAME, response.getFirstName());
        assertEquals(LASTNAME, response.getLastName());
        assertEquals(PHONE, response.getPhone());
        assertEquals(EMAIL, response.getEmail());
    }

    @Test
    public void whenGetCoordinatorByIdThenReturnAnObjectNotFoundException() {
        Mockito.when(coordinatorRepository.findById(Mockito.anyLong())).thenThrow(new EntityNotFoundException("Coordinator not found with id:" + ID));

        try {
            coordinatorService.getCoordinatorById(ID);
        } catch (Exception ex){
            assertEquals(EntityNotFoundException.class, ex.getClass());
            assertEquals("Coordinator not found with id:" + ID, ex.getMessage());
        }
    }

    @Test
    void whenGetAllCoordinatorThenReturnAnListOfCoordinator() {
        when(coordinatorRepository.findAll()).thenReturn(List.of(coordinator));

        List<CoordinatorDtoResponse> response = coordinatorService.getAllCoordinators();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(ID, response.get(0).getId());
        assertEquals(FIRSTNAME, response.get(0).getFirstName());
        assertEquals(LASTNAME, response.get(0).getLastName());
        assertEquals(PHONE, response.get(0).getPhone());
        assertEquals(EMAIL, response.get(0).getEmail());
    }

    @Test
    void save() {
        when(coordinatorRepository.save(any())).thenReturn(coordinator);

        CoordinatorDtoResponse response = coordinatorService.save(coordinatorDtoRequest);

        assertNotNull(response);
        assertEquals(CoordinatorDtoResponse.class, response.getClass());
        assertEquals(FIRSTNAME, response.getFirstName());
        assertEquals(LASTNAME, response.getLastName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PHONE, response.getPhone());


    }

    private void startUser(){
        coordinator = new Coordinator(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
        coordinatorDtoRequest = new CoordinatorDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }

}