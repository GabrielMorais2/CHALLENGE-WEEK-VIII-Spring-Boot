package gabriel.moraes.school.controller;

import gabriel.moraes.school.Model.DtoRequest.CoordinatorDtoRequest;
import gabriel.moraes.school.Model.DtoResponse.CoordinatorDtoResponse;
import gabriel.moraes.school.service.CoordinatorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coordinators")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
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
