package gabriel.moraes.school.service;

import gabriel.moraes.school.domain.coordinator.Coordinator;
import gabriel.moraes.school.domain.coordinator.CoordinatorRepository;
import gabriel.moraes.school.domain.coordinator.CoordinatorService;
import gabriel.moraes.school.domain.coordinator.dto.CoordinatorDtoRequest;
import gabriel.moraes.school.domain.coordinator.dto.CoordinatorDtoResponse;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    @Spy
    private ModelMapper mapper;

    private Coordinator coordinator;
    private CoordinatorDtoRequest coordinatorDtoRequest;

    @BeforeEach
    public void setup() {
        setupTestData();
    }

    @Test
    public void getCoordinatorById_ReturnAnCoordinatorDtoResponse() {
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
    public void getCoordinatorById_WithInvalidId_ReturnAnObjectNotFoundException() {
        Mockito.when(coordinatorRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> coordinatorService.getCoordinatorById(anyLong()));
    }

    @Test
    void getAllCoordinator_ReturnAnListOfCoordinatorDtoResponse() {
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
    void saveCoordinator_ReturnAnCoordinatorDtoResponse() {
        when(coordinatorRepository.save(any())).thenReturn(coordinator);

        CoordinatorDtoResponse response = coordinatorService.save(coordinatorDtoRequest);

        assertNotNull(response);
        assertEquals(CoordinatorDtoResponse.class, response.getClass());
        assertEquals(FIRSTNAME, response.getFirstName());
        assertEquals(LASTNAME, response.getLastName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PHONE, response.getPhone());


    }

    private void setupTestData(){
        coordinator = new Coordinator(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
        coordinatorDtoRequest = new CoordinatorDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }

}