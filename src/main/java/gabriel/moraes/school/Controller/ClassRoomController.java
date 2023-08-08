package gabriel.moraes.school.Controller;

import gabriel.moraes.school.Model.employee.DtoRequest.ClassRoomDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.ClassRoomDtoResponse;
import gabriel.moraes.school.Service.ClassRoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/class")
public class ClassRoomController {

    private final ClassRoomService classService;

    public ClassRoomController(ClassRoomService classService){
        this.classService = classService;
    }

    @PostMapping
    public ResponseEntity<ClassRoomDtoResponse> createClass(@Valid @RequestBody ClassRoomDtoRequest classRoomDtoRequest){
        ClassRoomDtoResponse dto = classService.createClass(classRoomDtoRequest);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

}
