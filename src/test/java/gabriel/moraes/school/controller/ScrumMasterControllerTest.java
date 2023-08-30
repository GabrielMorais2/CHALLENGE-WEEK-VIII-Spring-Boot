package gabriel.moraes.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabriel.moraes.school.domain.scrummaster.ScrumMasterController;
import gabriel.moraes.school.domain.scrummaster.dto.ScrumMasterDtoRequest;
import gabriel.moraes.school.domain.scrummaster.dto.ScrumMasterDtoResponse;
import gabriel.moraes.school.domain.scrummaster.ScrumMasterService;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScrumMasterController.class)
class ScrumMasterControllerTest {
    public static final Long ID = 1L;
    public static final String FIRSTNAME = "Gabriel";
    public static final String LASTNAME = "Moraes";
    public static final String EMAIL = "gabriel@moraes";
    public static final String PHONE = "81984458436";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScrumMasterService scrumMasterService;

    private ScrumMasterDtoRequest scrumMasterDtoRequest;
    private ScrumMasterDtoResponse scrumMasterDtoResponse;

    @BeforeEach
    public void setup() {
        scrumMasterDtoRequest = new ScrumMasterDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
        scrumMasterDtoResponse = new ScrumMasterDtoResponse(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }


    @Test
    void saveScrumMaster_withValidData_ReturnCreated() throws Exception {
        when(scrumMasterService.save(scrumMasterDtoRequest)).thenReturn(scrumMasterDtoResponse);

        mockMvc.perform(post("/api/v1/scrum-masters")
                        .content(objectMapper.writeValueAsString(scrumMasterDtoResponse))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(LASTNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phone").value(PHONE));
    }

    @Test
    void saveScrumMaster_withInvalidData_ReturnBadRequest() throws Exception {
        ScrumMasterDtoRequest emptyScrumMasterDtoRequest = new ScrumMasterDtoRequest("", "", "", "");
        ScrumMasterDtoRequest invalidScrumMasterDtoRequest = new ScrumMasterDtoRequest(null, null, null, null);

        mockMvc.perform(post("/api/v1/scrum-masters")
                        .content(objectMapper.writeValueAsString(emptyScrumMasterDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/scrum-masters")
                        .content(objectMapper.writeValueAsString(invalidScrumMasterDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getScrumMasterById_ExistingId_ReturnScrumMaster() throws Exception {
        when(scrumMasterService.getScrumMasterById(anyLong())).thenReturn(scrumMasterDtoResponse);

        mockMvc.perform(get("/api/v1/scrum-masters/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(LASTNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phone").value(PHONE));
    }

    @Test
    void getScrumMasterById_UnexistingId_ReturnsObjectNotFound() throws Exception {
        when(scrumMasterService.getScrumMasterById(anyLong())).thenThrow(new ObjectNotFoundException("Scrum Master not found"));

        mockMvc.perform(get("/api/v1/scrum-masters/{id}", 2L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ObjectNotFoundException))
                .andExpect(result -> assertEquals("Scrum Master not found", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void getAllScrumMasters_ReturnAnListScrumMasters() throws Exception {
        List<ScrumMasterDtoResponse> scrumMasterDtoResponseList = Arrays.asList(scrumMasterDtoResponse, scrumMasterDtoResponse);
        when(scrumMasterService.getAllScrumMasters()).thenReturn(scrumMasterDtoResponseList);

        mockMvc.perform(get("/api/v1/scrum-masters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$[0].lastName").value(LASTNAME))
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[0].phone").value(PHONE));
    }

    @Test
    void getAllCoordinators_ReturnNoCoordinators() throws Exception {
        when(scrumMasterService.getAllScrumMasters()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/scrum-masters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

}