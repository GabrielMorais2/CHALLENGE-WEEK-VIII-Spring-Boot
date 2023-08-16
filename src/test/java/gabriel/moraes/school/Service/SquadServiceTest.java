package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.ClassRoom;
import gabriel.moraes.school.Model.ClassStatus;
import gabriel.moraes.school.Model.Squad;
import gabriel.moraes.school.Model.employee.DtoResponse.SquadDtoResponse;
import gabriel.moraes.school.Utils.JsonUtils;
import gabriel.moraes.school.exception.InvalidClassStatusException;
import gabriel.moraes.school.exception.NoRegisteredStudentsException;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import gabriel.moraes.school.repository.ClassRoomRepository;
import gabriel.moraes.school.repository.SquadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SquadServiceTest {

    @Mock
    private SquadRepository squadRepository;
    @Mock
    private ClassRoomRepository classRoomRepository;
    @InjectMocks
    private SquadService squadService;
    @Spy
    private ModelMapper mapper;

    private static final String CLASSROOM = "Payload/ClassRoom/CLASSROOM_WITH_STUDENTS_AND_START.json";
    private static final String SQUAD = "Payload/Squad/Squads.json";
    private static final String SQUAD_UPDATENAME = "Payload/Squad/SQUAD_UPDATENAME.json";

    @Test
    void createSquad_ReturnSquadDtoResponse() throws IOException {

        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM, ClassRoom.class);
        Squad[] squads = JsonUtils.getObjectFromFile(SQUAD, Squad[].class);

        when(classRoomRepository.findById(anyLong())).thenReturn(Optional.of(classRoom));

        when(squadRepository.saveAll(anyList())).thenReturn(List.of(squads));

        List<SquadDtoResponse> response = squadService.createSquad(1L);

        assertNotNull(response.getClass());
        assertEquals(response.get(0).getId(), 1L);
        assertEquals(response.get(0).getName(), "Uninformed");
        assertEquals(response.get(1).getId(), 2L);
        assertEquals(response.get(2).getId(), 3L);
    }

    @Test
    void updateSquadName_ReturnSquadDtoResponseWithNameUpdate() throws IOException {

        Squad[] squads = JsonUtils.getObjectFromFile(SQUAD_UPDATENAME, Squad[].class);

        String newSquadName = "New Squad Name";

        when(squadRepository.findById(anyLong())).thenReturn(Optional.ofNullable(squads[0]));

        when(squadRepository.save(any())).thenReturn(squads[0]);

        SquadDtoResponse response = squadService.updateSquadName(1L, newSquadName);

        verify(squadRepository).save(squads[0]);

        assertNotNull(response);

        assertEquals(newSquadName, squads[0].getName());
        assertEquals(squads[0].getId(), response.getId());
        assertEquals(newSquadName, response.getName());
    }
    @Test
    void createSquad_WithInvalidIdClassroom_ReturnAnObjectNotFoundException(){
        when(classRoomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> squadService.createSquad(anyLong()));
    }

    @Test
    void whenCreateSquadThenReturnAnInvalidClassStatusException() throws IOException {
        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM, ClassRoom.class);
        classRoom.setStatus(ClassStatus.WAITING);

        when(classRoomRepository.findById(anyLong())).thenReturn(Optional.of(classRoom));

        assertThrows(InvalidClassStatusException.class, () -> squadService.createSquad(anyLong()));
    }

    @Test
    void whenCreateSquadThenReturnAnNoRegisteredStudentsException() throws IOException {
        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM, ClassRoom.class);
        classRoom.setStudents(new ArrayList<>());

        when(classRoomRepository.findById(anyLong())).thenReturn(Optional.of(classRoom));

        assertThrows(NoRegisteredStudentsException.class, () -> squadService.createSquad(anyLong()));
    }
}