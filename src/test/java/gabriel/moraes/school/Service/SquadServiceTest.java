package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.ClassRoom;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SquadServiceTest {

    public static final long ID = 1L;

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
    private static final String CLASSROOM_COMPLETE = "Payload/ClassRoom/CLASSROOM_WITH_STUDENTS_AND_SQUAD.json";



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

        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM_COMPLETE, ClassRoom.class);
        Squad[] squads = JsonUtils.getObjectFromFile(SQUAD, Squad[].class);

        String newSquadName = "New Squad Name";

        when(classRoomRepository.findById(anyLong())).thenReturn(Optional.of(classRoom));
        when(squadRepository.save(any())).thenReturn(squads);

        SquadDtoResponse response = squadService.updateSquadName(ID, ID, newSquadName);

        verify(classRoomRepository).findById(1L);
        //verify(squadRepository).save(squads.get(0));

        assertNotNull(response);

        //assertEquals(newSquadName, squads.get(0).getName());
        //assertEquals(squads.get(0).getId(), response.getId());
        assertEquals(newSquadName, response.getName());
    }
    @Test
    void whenCreateSquadThenReturnAnClassRoomInvalidObjectNotFoundException() throws IOException {

        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM, ClassRoom.class);
        List<Squad> squads = Collections.singletonList(JsonUtils.getObjectFromFile(SQUAD, Squad.class));

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
    void whenCreateSquadThenReturnAnNoRegisteredStudentsException() throws IOException {

        ClassRoom classRoom = JsonUtils.getObjectFromFile(CLASSROOM, ClassRoom.class);
        List<Squad> squads = Collections.singletonList(JsonUtils.getObjectFromFile(SQUAD, Squad.class));

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
}