package gabriel.moraes.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabriel.moraes.school.Model.DtoRequest.CoordinatorDtoRequest;
import gabriel.moraes.school.Model.DtoResponse.CoordinatorDtoResponse;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import gabriel.moraes.school.service.CoordinatorService;
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

@WebMvcTest(CoordinatorController.class)
class CoordinatorControllerTest {

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
    private CoordinatorService coordinatorService;

    private CoordinatorDtoRequest coordinatorDtoRequest;
    private CoordinatorDtoResponse coordinatorDtoResponse;

    @BeforeEach
    public void setup() {
        coordinatorDtoRequest = new CoordinatorDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
        coordinatorDtoResponse = new CoordinatorDtoResponse(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }


    @Test
    void saveCoordinator_withValidData_ReturnCreated() throws Exception {
        when(coordinatorService.save(coordinatorDtoRequest)).thenReturn(coordinatorDtoResponse);

        mockMvc.perform(post("/api/v1/coordinators")
                        .content(objectMapper.writeValueAsString(coordinatorDtoResponse))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(LASTNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phone").value(PHONE));
    }

    @Test
    void saveCoordinator_withEmptyData_ReturnBadRequest() throws Exception {
        CoordinatorDtoRequest emptyCoordinatorDtoRequest = new CoordinatorDtoRequest("", "", "", "");

        mockMvc.perform(post("/api/v1/coordinators")
                        .content(objectMapper.writeValueAsString(emptyCoordinatorDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveCoordinator_withInvalidData_ReturnBadRequest() throws Exception {
        CoordinatorDtoRequest invalidCoordinatorDtoRequest = new CoordinatorDtoRequest(null, null, null, null);

        mockMvc.perform(post("/api/v1/coordinators")
                        .content(objectMapper.writeValueAsString(invalidCoordinatorDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCoordinatorById_ExistingId_ReturnCoordinator() throws Exception {
        when(coordinatorService.getCoordinatorById(anyLong())).thenReturn(coordinatorDtoResponse);

        mockMvc.perform(get("/api/v1/coordinators/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(LASTNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phone").value(PHONE));
    }

    @Test
    void getCoordinatorById_UnexistingId_ReturnsObjectNotFound() throws Exception {
        when(coordinatorService.getCoordinatorById(anyLong())).thenThrow(new ObjectNotFoundException("Coordinator not found"));

        mockMvc.perform(get("/api/v1/coordinators/{id}", 2L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ObjectNotFoundException))
                .andExpect(result -> assertEquals("Coordinator not found", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void getAllCoordinators_ReturnAnListCoordinators() throws Exception {
        List<CoordinatorDtoResponse> coordinatorDtoResponseList = Arrays.asList(coordinatorDtoResponse, coordinatorDtoResponse);
        when(coordinatorService.getAllCoordinators()).thenReturn(coordinatorDtoResponseList);

        mockMvc.perform(get("/api/v1/coordinators"))
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
        when(coordinatorService.getAllCoordinators()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/coordinators"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}