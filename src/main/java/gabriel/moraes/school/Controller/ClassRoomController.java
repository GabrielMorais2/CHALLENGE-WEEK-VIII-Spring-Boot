package gabriel.moraes.school.Controller;

import gabriel.moraes.school.Model.ClassRoom;
import gabriel.moraes.school.Model.employee.DtoRequest.AddStudentsDtoRequest;
import gabriel.moraes.school.Model.employee.DtoRequest.ClassRoomDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.ClassRoomDtoResponse;
import gabriel.moraes.school.Service.ClassRoomService;
import gabriel.moraes.school.repository.ClassRoomRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/class")
public class ClassRoomController {
    private final ClassRoomService classService;

    public ClassRoomController(ClassRoomService classService){
        this.classService = classService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClassRoomDtoResponse> getClassById(@PathVariable Long id){
        ClassRoomDtoResponse dto = classService.getClassById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ClassRoomDtoResponse> createClass(@Valid @RequestBody ClassRoomDtoRequest classRoomDtoRequest){
        ClassRoomDtoResponse dto = classService.createClass(classRoomDtoRequest);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/add-students")
    public ResponseEntity<ClassRoomDtoResponse> addStudentsToClass(
            @PathVariable Long id,
            @RequestBody AddStudentsDtoRequest addStudentsDtoRequest) {

        ClassRoomDtoResponse response = classService.addStudentsToClass(id, addStudentsDtoRequest);

        return ResponseEntity.ok(response);
    }

}
