package gabriel.moraes.school.Controller;

import gabriel.moraes.school.Model.Student;
import gabriel.moraes.school.Model.employee.DtoRequest.CoordinatorDtoRequest;
import gabriel.moraes.school.Model.employee.DtoRequest.UpdateSquadNameRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.SquadDtoResponse;
import gabriel.moraes.school.Service.SquadService;
import gabriel.moraes.school.repository.SquadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class SquadControllerTest {

    @Mock
    private SquadRepository squadRepository;
    @InjectMocks
    private SquadController squadController;
    @Mock
    private SquadService squadService;
    private SquadDtoResponse squadDtoResponse;
    private UpdateSquadNameRequest updateSquadNameRequest;
    private List<Student> students;

    @BeforeEach
    void beforeAll() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    @Test
    void updateSquadName() {
        String newName = "Kaisen";
        SquadDtoResponse squadDtoResponseUpdate = new SquadDtoResponse(1L, "Kaisen", students);

        when(squadService.updateSquadName(anyLong(), anyLong(), anyString())).thenReturn(squadDtoResponseUpdate);

        ResponseEntity<SquadDtoResponse> response = squadController.updateSquadName(1L, 1L, updateSquadNameRequest);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(newName, response.getBody().getName());
        assertEquals(squadDtoResponse.getId(), response.getBody().getId());
    }

    @Test
    void createClass() {
        when(squadService.createSquad(anyLong())).thenReturn(List.of(squadDtoResponse));

        ResponseEntity<List<SquadDtoResponse>> response = squadController.createClass(1L);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(SquadDtoResponse.class, response.getBody().get(0).getClass());

        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals("The Fofoca Brokers", response.getBody().get(0).getName());
        assertEquals(students, response.getBody().get(0).getStudents());

    }

    private void setupTestData(){
        students = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            students.add(new Student());
        }

        squadDtoResponse = new SquadDtoResponse(1L, "The Fofoca Brokers", students);
        updateSquadNameRequest = new UpdateSquadNameRequest("Kaisen");

    }

}