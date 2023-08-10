package gabriel.moraes.school.Controller;

import gabriel.moraes.school.Model.employee.DtoRequest.InstructorDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.InstructorDtoResponse;
import gabriel.moraes.school.Service.InstructorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService){
        this.instructorService = instructorService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<InstructorDtoResponse> getInstructorById(@PathVariable Long id) {
        InstructorDtoResponse dto = instructorService.getInstructorById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<InstructorDtoResponse>> getAllInstructors() {
        List<InstructorDtoResponse> dtos = instructorService.getAllInstructors();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InstructorDtoResponse> save(@Valid @RequestBody InstructorDtoRequest instructorDtoRequest){
        InstructorDtoResponse dto = instructorService.save(instructorDtoRequest);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
