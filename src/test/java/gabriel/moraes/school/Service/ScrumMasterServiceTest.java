package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.employee.DtoRequest.ScrumMasterDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.ScrumMasterDtoResponse;
import gabriel.moraes.school.Model.employee.ScrumMaster;
import gabriel.moraes.school.repository.ScrumMasterRepository;
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

class ScrumMasterServiceTest {

    public static final Long ID = 1L;
    public static final String FIRSTNAME = "Gabriel";
    public static final String LASTNAME = "Moraes";
    public static final String EMAIL = "gabriel@moraes";
    public static final String PHONE = "81984458436";

    @Mock
    private ScrumMasterRepository scrumMasterRepository;
    @InjectMocks
    private ScrumMasterService scrumMasterService;

    private ScrumMaster scrumMaster;
    private ScrumMasterDtoRequest scrumMasterDtoRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ModelMapper mapper = new ModelMapper();
        scrumMasterService = new ScrumMasterService(scrumMasterRepository, mapper);
        startUser();
    }
    @Test
    public void whenGetScrumMasterByIdThenReturnAnCoordinator() {
        Mockito.when(scrumMasterRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(scrumMaster));

        ScrumMasterDtoResponse response = scrumMasterService.getScrumMasterById(ID);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals(FIRSTNAME, response.getFirstName());
        assertEquals(LASTNAME, response.getLastName());
        assertEquals(PHONE, response.getPhone());
        assertEquals(EMAIL, response.getEmail());
    }

    @Test
    public void whenGetScrumMasterByIdThenReturnAnEntityNotFoundException() {
        Mockito.when(scrumMasterRepository.findById(Mockito.anyLong())).thenThrow(new EntityNotFoundException("Scrum Master not found with id:" + ID));

        try {
            scrumMasterService.getScrumMasterById(ID);
        } catch (Exception ex){
            assertEquals(EntityNotFoundException.class, ex.getClass());
            assertEquals("Scrum Master not found with id:" + ID, ex.getMessage());
        }
    }

    @Test
    void whenGetAllScrumMastersThenReturnAnListOfScrumMasters() {
        when(scrumMasterRepository.findAll()).thenReturn(List.of(scrumMaster));

        List<ScrumMasterDtoResponse> response = scrumMasterService.getAllScrumMasters();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(ID, response.get(0).getId());
        assertEquals(FIRSTNAME, response.get(0).getFirstName());
        assertEquals(LASTNAME, response.get(0).getLastName());
        assertEquals(PHONE, response.get(0).getPhone());
        assertEquals(EMAIL, response.get(0).getEmail());
    }

    @Test
    void WhenSaveScrumMasterThenReturnAnScrumMasterDtoResponse() {
        when(scrumMasterRepository.save(any())).thenReturn(scrumMaster);

        ScrumMasterDtoResponse response = scrumMasterService.save(scrumMasterDtoRequest);

        assertNotNull(response);
        assertEquals(ScrumMasterDtoResponse.class, response.getClass());
        assertEquals(FIRSTNAME, response.getFirstName());
        assertEquals(LASTNAME, response.getLastName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PHONE, response.getPhone());


    }

    private void startUser(){
        scrumMaster = new ScrumMaster(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
        scrumMasterDtoRequest = new ScrumMasterDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }
}