package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.ClassRoom;
import gabriel.moraes.school.Model.ClassStatus;
import gabriel.moraes.school.Model.Squad;
import gabriel.moraes.school.Model.Student;
import gabriel.moraes.school.Model.employee.Coordinator;
import gabriel.moraes.school.Model.employee.DtoResponse.SquadDtoResponse;
import gabriel.moraes.school.Model.employee.Instructor;
import gabriel.moraes.school.Model.employee.ScrumMaster;
import gabriel.moraes.school.exception.InvalidClassStatusException;
import gabriel.moraes.school.exception.NoRegisteredStudentsException;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import gabriel.moraes.school.repository.ClassRoomRepository;
import gabriel.moraes.school.repository.SquadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SquadServiceTest {

    public static final long ID = 1L;
    @Mock
    private SquadRepository squadRepository;
    @Mock
    private ClassRoomRepository classRoomRepository;
    @InjectMocks
    private SquadService squadService;
    private ClassRoom classRoom;
    private List<Squad> squads;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ModelMapper mapper = new ModelMapper();
        squadService = new SquadService(classRoomRepository, squadRepository, mapper);
        setupTestData();
    }

    @Test
    void whenCreateSquadThenReturnSquadDtoResponse() {
        classRoom.setStatus(ClassStatus.STARTED);
        when(classRoomRepository.findById(anyLong())).thenReturn(Optional.of(classRoom));
        when(squadRepository.saveAll(anyList())).thenReturn(squads);

        List<SquadDtoResponse> response = squadService.createSquad(1L);

        assertNotNull(response.getClass());
        assertEquals(response.get(0).getId(), 1L);
        assertEquals(response.get(0).getName(), "The Fofoca Brockers");
        assertEquals(response.get(0).getStudents(), classRoom.getStudents().subList(0,4));
        assertEquals(response.get(1).getId(), 2L);
        assertEquals(response.get(2).getId(), 3L);
    }

    @Test
    void whenUpdateSquadNameThenReturnSquadDtoResponseWithNameUpdate() {
        classRoom.setStatus(ClassStatus.STARTED);
        classRoom.setSquads(squads);
        String newSquadName = "New Squad Name";
        Long squadIdToUpdate = 1L;

        when(classRoomRepository.findById(anyLong())).thenReturn(Optional.of(classRoom));
        when(squadRepository.save(any())).thenReturn(squads.get(0));

        SquadDtoResponse response = squadService.updateSquadName(ID, squadIdToUpdate, newSquadName);

        verify(classRoomRepository).findById(1L);
        verify(squadRepository).save(squads.get(0));

        assertNotNull(response);

        assertEquals(newSquadName, squads.get(0).getName());
        assertEquals(squads.get(0).getId(), response.getId());
        assertEquals(newSquadName, response.getName());
    }
    @Test
    void whenCreateSquadThenReturnAnClassRoomInvalidObjectNotFoundException() {
        classRoom.setStatus(ClassStatus.STARTED);

        when(classRoomRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException("Class room not found with id:" + ID));

        try {
            squadService.createSquad(ID);
        } catch (Exception ex){
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals("Class room not found with id:" + ID, ex.getMessage());
        }
    }

    @Test
    void whenCreateSquadThenReturnAnInvalidClassStatusException() {
        when(classRoomRepository.findById(anyLong())).thenThrow(new InvalidClassStatusException("It is only possible to create squads when a class is started."));

        try {
            squadService.createSquad(ID);
        } catch (Exception ex) {
            assertEquals(InvalidClassStatusException.class, ex.getClass());
            assertEquals("It is only possible to create squads when a class is started.", ex.getMessage());

            verify(classRoomRepository).findById(ID);
        }
    }

    @Test
    void whenCreateSquadThenReturnAnNoRegisteredStudentsException() {
        classRoom.setStatus(ClassStatus.STARTED);
        classRoom.setStudents(new ArrayList<>());

        when(classRoomRepository.findById(anyLong())).thenThrow(new NoRegisteredStudentsException("There are no registered students."));

        try {
            squadService.createSquad(ID);
        } catch (Exception ex) {
            assertEquals(NoRegisteredStudentsException.class, ex.getClass());
            assertEquals("There are no registered students.", ex.getMessage());

            verify(classRoomRepository).findById(ID);
        }

        verify(classRoomRepository).findById(ID);

    }

    private void setupTestData() {

        List<Student> students = new ArrayList<>();
        List<Instructor> instructors = new ArrayList<>();
        List<ScrumMaster> scrumMasters = new ArrayList<>();
        List<Coordinator> coordinators = new ArrayList<>();
        squads = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            students.add(new Student());
        }

        for (int i = 0; i < 3; i++) {
            instructors.add(new Instructor());
        }
        scrumMasters.add(new ScrumMaster());
        coordinators.add(new Coordinator());
        classRoom = new ClassRoom(1L, "Spring Program", ClassStatus.WAITING, coordinators, scrumMasters, instructors, students);
        classRoom.setStudents(students);

        Squad squad = new Squad(1L, "The Fofoca Brockers", classRoom, students.subList(0, 4));
        Squad squad2 = new Squad(2L, "The Fofoca Brockers", classRoom, students.subList(5,9));
        Squad squad3 = new Squad(3L, "The Fofoca Brockers", classRoom, students.subList(10,14));
        squads.add(squad);
        squads.add(squad2);
        squads.add(squad3);

    }
}