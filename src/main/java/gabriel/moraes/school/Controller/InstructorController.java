package gabriel.moraes.school.Controller;

import gabriel.moraes.school.Model.employee.DtoRequest.InstructorDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.InstructorDtoResponse;
import gabriel.moraes.school.Service.InstructorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService){
        this.instructorService = instructorService;
    }

    @PostMapping
    public ResponseEntity<InstructorDtoResponse> save(@Valid @RequestBody InstructorDtoRequest instructorDtoRequest){
        InstructorDtoResponse dto = instructorService.save(instructorDtoRequest);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
