package gabriel.moraes.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabriel.moraes.school.domain.instructor.InstructorController;
import gabriel.moraes.school.domain.instructor.InstructorService;
import gabriel.moraes.school.domain.instructor.dto.InstructorDtoRequest;
import gabriel.moraes.school.domain.instructor.dto.InstructorDtoResponse;
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
@WebMvcTest(InstructorController.class)
class InstructorControllerTest {
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
    private InstructorService instructorService;

    private InstructorDtoRequest instructorDtoRequest;
    private InstructorDtoResponse instructorDtoResponse;


    @BeforeEach
    public void setup() {
        instructorDtoRequest = new InstructorDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
        instructorDtoResponse = new InstructorDtoResponse(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }



    @Test
    void saveInstructor_withValidData_ReturnCreated() throws Exception {
        when(instructorService.save(instructorDtoRequest)).thenReturn(instructorDtoResponse);

        mockMvc.perform(post("/api/v1/instructors")
                        .content(objectMapper.writeValueAsString(instructorDtoResponse))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(LASTNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phone").value(PHONE));
    }

    @Test
    void saveInstructor_withInvalidData_ReturnBadRequest() throws Exception {
        InstructorDtoRequest emptyInstructorsDtoRequest = new InstructorDtoRequest("", "", "", "");
        InstructorDtoRequest invalidInstructorDtoRequest = new InstructorDtoRequest(null, null, null, null);

        mockMvc.perform(post("/api/v1/instructors")
                        .content(objectMapper.writeValueAsString(emptyInstructorsDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/instructors")
                        .content(objectMapper.writeValueAsString(invalidInstructorDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getInstructorById_ExistingId_ReturnInstructor() throws Exception {
        when(instructorService.getInstructorById(anyLong())).thenReturn(instructorDtoResponse);

        mockMvc.perform(get("/api/v1/instructors/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(LASTNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phone").value(PHONE));
    }

    @Test
    void getInstructorById_UnexistingId_ReturnsObjectNotFound() throws Exception {
        when(instructorService.getInstructorById(anyLong())).thenThrow(new ObjectNotFoundException("instructors not found"));

        mockMvc.perform(get("/api/v1/instructors/{id}", 2L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ObjectNotFoundException))
                .andExpect(result -> assertEquals("instructors not found", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void getAllInstructors_ReturnAnListInstructors() throws Exception {
        List<InstructorDtoResponse> instructorDtoResponsesList = Arrays.asList(instructorDtoResponse, instructorDtoResponse);
        when(instructorService.getAllInstructors()).thenReturn(instructorDtoResponsesList);

        mockMvc.perform(get("/api/v1/instructors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$[0].lastName").value(LASTNAME))
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[0].phone").value(PHONE));
    }

    @Test
    void getAllInstructors_ReturnNoInstructors() throws Exception {
        when(instructorService.getAllInstructors()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/instructors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

}