package gabriel.moraes.school.service;

import gabriel.moraes.school.Model.DtoRequest.ScrumMasterDtoRequest;
import gabriel.moraes.school.Model.DtoResponse.ScrumMasterDtoResponse;
import gabriel.moraes.school.Model.ScrumMaster;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import gabriel.moraes.school.repository.ScrumMasterRepository;
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
    @Spy
    private ModelMapper mapper;

    @BeforeEach
    public void setup() {
        setupTestData();
    }
    @Test
    public void getScrumMasterById_ReturnAnScrumMasterDtoResponse() {
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
    public void getScrumMasterById_WithInvalidId_ReturnAnObjectNotFoundException() {
        Mockito.when(scrumMasterRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> scrumMasterService.getScrumMasterById(anyLong()));
    }

    @Test
    void getAllScrumMasters_ReturnAnListOfScrumMasterDtoResponse() {
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
    void saveScrumMaster_ReturnAnScrumMasterDtoResponse() {
        when(scrumMasterRepository.save(any())).thenReturn(scrumMaster);

        ScrumMasterDtoResponse response = scrumMasterService.save(scrumMasterDtoRequest);

        assertNotNull(response);
        assertEquals(ScrumMasterDtoResponse.class, response.getClass());
        assertEquals(FIRSTNAME, response.getFirstName());
        assertEquals(LASTNAME, response.getLastName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PHONE, response.getPhone());


    }

    private void setupTestData(){
        scrumMaster = new ScrumMaster(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
        scrumMasterDtoRequest = new ScrumMasterDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }
}