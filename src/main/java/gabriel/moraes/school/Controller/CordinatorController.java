package gabriel.moraes.school.Controller;

import gabriel.moraes.school.Model.employee.DtoRequest.CoordinatorDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.CoordinatorDtoResponse;
import gabriel.moraes.school.Service.CoordinatorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coordinators")
public class CordinatorController {

    private final CoordinatorService coordinatorService;

    public CordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<CoordinatorDtoResponse> getCoordinatorById(@PathVariable Long id) {
        CoordinatorDtoResponse dto = coordinatorService.getCoordinatorById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CoordinatorDtoResponse>> getAllCoordinator() {
        List<CoordinatorDtoResponse> dtos = coordinatorService.getAllCoordinators();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CoordinatorDtoResponse> save(@Valid @RequestBody CoordinatorDtoRequest coordinatorDtoRequest){
        CoordinatorDtoResponse dto = coordinatorService.save(coordinatorDtoRequest);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
