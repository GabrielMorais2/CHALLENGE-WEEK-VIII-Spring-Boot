package gabriel.moraes.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabriel.moraes.school.domain.classroom.ClassRoomController;
import gabriel.moraes.school.domain.classroom.ClassRoomService;
import gabriel.moraes.school.domain.classroom.ClassStatus;
import gabriel.moraes.school.domain.classroom.dto.AddStudentsDtoRequest;
import gabriel.moraes.school.domain.classroom.dto.ClassRoomDtoRequest;
import gabriel.moraes.school.domain.classroom.dto.ClassRoomDtoResponse;
import gabriel.moraes.school.domain.coordinator.Coordinator;
import gabriel.moraes.school.domain.instructor.Instructor;
import gabriel.moraes.school.domain.scrummaster.ScrumMaster;
import gabriel.moraes.school.domain.student.Student;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ClassRoomController.class)
class ClassRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClassRoomService classRoomService;
    private ClassRoomDtoRequest classRoomDtoRequest;
    private ClassRoomDtoResponse classRoomDtoResponse;
    private AddStudentsDtoRequest addStudentsDtoRequest;

    @BeforeEach
    public void setup() {
        classRoomDtoRequest = new ClassRoomDtoRequest("the fofoca brockers", List.of(1L), List.of(1L), List.of(1L));
        classRoomDtoResponse = new ClassRoomDtoResponse(1L, "the fofoca brockers", ClassStatus.WAITING, List.of(new Coordinator()), List.of(new ScrumMaster()), List.of(new Instructor()));
        addStudentsDtoRequest = new AddStudentsDtoRequest(List.of(1L));
    }


    @Test
    public void getClassById_ExistingId_ReturnClass() throws Exception {
        Long classId = 1L;

        Mockito.when(classRoomService.getClassById(classId)).thenReturn(classRoomDtoResponse);

        mockMvc.perform(get("/api/v1/classes/{id}", classId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(classId));
    }

    @Test
    void getClassById_UnexistingId_ReturnsObjectNotFound() throws Exception {
        when(classRoomService.getClassById(anyLong())).thenThrow(new ObjectNotFoundException("Class room not found"));

        mockMvc.perform(get("/api/v1/classes/{id}", 2L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ObjectNotFoundException))
                .andExpect(result -> assertEquals("Class room not found", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void createClass_ReturnCreated() throws Exception {
        Long classId = 1L;

        Mockito.when(classRoomService.createClass(classRoomDtoRequest)).thenReturn(classRoomDtoResponse);

        mockMvc.perform(post("/api/v1/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(classRoomDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(classId))
                .andExpect(jsonPath("$.name").value("the fofoca brockers"));
    }

    @Test
    void addStudentsToClass_ReturnSuccess() throws Exception {
        Long classId = 1L;

        ClassRoomDtoResponse classRoomDtoResponseWithStudents = new ClassRoomDtoResponse(1L, "the fofoca brockers", ClassStatus.WAITING, List.of(new Coordinator()), List.of(new ScrumMaster()), List.of(new Instructor()), List.of(new Student()));

        Mockito.when(classRoomService.addStudentsToClass(classId, addStudentsDtoRequest)).thenReturn(classRoomDtoResponseWithStudents);

        mockMvc.perform(patch("/api/v1/classes/{id}/add-students", classId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addStudentsDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(classId))
                .andExpect(jsonPath("$.name").value("the fofoca brockers"))
                .andExpect(jsonPath("$.students").isArray());
    }

    @Test
    public void startClass_ReturnSuccess() throws Exception {
        Long classId = 1L;

        mockMvc.perform(patch("/api/v1/classes/{id}/start", classId))
                .andExpect(status().isNoContent());
    }
    @Test
    public void testFinishClass() throws Exception {
        Long classId = 1L;

        mockMvc.perform(patch("/api/v1/classes/{id}/finish", classId))
                .andExpect(status().isNoContent());
    }
}