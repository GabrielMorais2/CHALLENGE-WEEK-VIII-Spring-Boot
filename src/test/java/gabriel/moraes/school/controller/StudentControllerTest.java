package gabriel.moraes.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabriel.moraes.school.Model.DtoRequest.StudentDtoRequest;
import gabriel.moraes.school.Model.DtoResponse.StudentDtoResponse;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import gabriel.moraes.school.service.StudentService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(StudentController.class)
class StudentControllerTest {

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
    private StudentService studentService;

    private StudentDtoRequest studentDtoRequest;
    private StudentDtoResponse studentDtoResponse;

    @BeforeEach
    public void setup() {
        studentDtoRequest = new StudentDtoRequest(FIRSTNAME, LASTNAME, EMAIL, PHONE);
        studentDtoResponse = new StudentDtoResponse(ID, FIRSTNAME, LASTNAME, EMAIL, PHONE);
    }


    @Test
    void saveStudent_withValidData_ReturnCreated() throws Exception {
        when(studentService.save(studentDtoRequest)).thenReturn(studentDtoResponse);

        mockMvc.perform(post("/api/v1/students")
                        .content(objectMapper.writeValueAsString(studentDtoResponse))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(LASTNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phone").value(PHONE));
    }

    @Test
    void saveStudent_withInvalidData_ReturnBadRequest() throws Exception {
        StudentDtoRequest emptyStudentDtoRequest = new StudentDtoRequest("", "", "", "");
        StudentDtoRequest invalidStudentDtoRequest = new StudentDtoRequest(null, null, null, null);

        mockMvc.perform(post("/api/v1/students")
                        .content(objectMapper.writeValueAsString(emptyStudentDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/students")
                        .content(objectMapper.writeValueAsString(invalidStudentDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStudentById_ExistingId_ReturnScrumMaster() throws Exception {
        when(studentService.getStudentById(anyLong())).thenReturn(studentDtoResponse);

        mockMvc.perform(get("/api/v1/students/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(LASTNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phone").value(PHONE));
    }

    @Test
    void getStudentById_UnexistingId_ReturnsObjectNotFound() throws Exception {
        when(studentService.getStudentById(anyLong())).thenThrow(new ObjectNotFoundException("Student not found"));

        mockMvc.perform(get("/api/v1/students/{id}", 2L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ObjectNotFoundException))
                .andExpect(result -> assertEquals("Student not found", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void getAllStudents_ReturnAnListStudents() throws Exception {
        List<StudentDtoResponse> studentDtoResponseList = Arrays.asList(studentDtoResponse, studentDtoResponse);
        when(studentService.getAllStudents()).thenReturn(studentDtoResponseList);

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$[0].lastName").value(LASTNAME))
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[0].phone").value(PHONE));
    }

    @Test
    void getAllStudents_ReturnNoStudents() throws Exception {
        when(studentService.getAllStudents()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void deleteStudent_WithExistingId_ReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/students/{id}", 1L))
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteStudent_WithUnexistingId_ReturnNotFound() throws Exception {
        doThrow(new ObjectNotFoundException("Id not found")).when(studentService).deleteStudentById(1L);

        mockMvc.perform(delete("/api/v1/students/{id}", 1L))
                .andExpect(status().isNotFound());

    }



}