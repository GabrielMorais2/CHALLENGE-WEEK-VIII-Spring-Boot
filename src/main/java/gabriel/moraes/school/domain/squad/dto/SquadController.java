package gabriel.moraes.school.domain.squad.dto;

import gabriel.moraes.school.domain.squad.SquadService;
import gabriel.moraes.school.domain.squad.UpdateSquadNameRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/squads")
public class SquadController {
    private final SquadService squadService;

    public SquadController(SquadService squadService){
        this.squadService = squadService;
    }
    @PatchMapping("/update-squad/{squadId}")
    public ResponseEntity<SquadDtoResponse> updateSquadName(
            @PathVariable Long squadId,
            @Valid @RequestBody UpdateSquadNameRequest updateSquadNameRequest) {

        SquadDtoResponse dtoResponse = squadService.updateSquadName(squadId, updateSquadNameRequest.getName());

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping("/{idClass}/create-squad")
    public ResponseEntity<List<SquadDtoResponse>> createClass(@PathVariable Long idClass) {
        List<SquadDtoResponse> dtoResponses = squadService.createSquad(idClass);
        return new ResponseEntity<>(dtoResponses, HttpStatus.CREATED);
    }

}
