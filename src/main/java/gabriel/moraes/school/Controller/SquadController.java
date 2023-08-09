package gabriel.moraes.school.Controller;

import gabriel.moraes.school.Model.Squad;
import gabriel.moraes.school.Model.employee.DtoRequest.AddStudentsDtoRequest;
import gabriel.moraes.school.Model.employee.DtoRequest.ClassRoomDtoRequest;
import gabriel.moraes.school.Model.employee.DtoRequest.SquadDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.ClassRoomDtoResponse;
import gabriel.moraes.school.Model.employee.DtoResponse.SquadDtoResponse;
import gabriel.moraes.school.Service.ClassRoomService;
import gabriel.moraes.school.Service.SquadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/squad")
public class SquadController {
    private final SquadService squadService;

    public SquadController(SquadService squadService){
        this.squadService = squadService;
    }


    @PostMapping("/{id}/create-squad")
    public ResponseEntity<List<SquadDtoResponse>> createClass(@PathVariable Long id) {
        List<SquadDtoResponse> dtoResponses = squadService.createSquad(id);
        return new ResponseEntity<>(dtoResponses, HttpStatus.OK);
    }

}
