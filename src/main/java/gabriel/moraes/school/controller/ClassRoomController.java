package gabriel.moraes.school.controller;

import gabriel.moraes.school.Model.DtoRequest.AddStudentsDtoRequest;
import gabriel.moraes.school.Model.DtoRequest.ClassRoomDtoRequest;
import gabriel.moraes.school.Model.DtoResponse.ClassRoomDtoResponse;
import gabriel.moraes.school.service.ClassRoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/classes")
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

    @PatchMapping("/{id}/start")
    public ResponseEntity<Void> startClass(@PathVariable Long id) {

        classService.startClass(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PatchMapping("/{id}/add-students")
    public ResponseEntity<ClassRoomDtoResponse> addStudentsToClass(
            @PathVariable Long id,
            @Valid @RequestBody AddStudentsDtoRequest addStudentsDtoRequest) {

        ClassRoomDtoResponse response = classService.addStudentsToClass(id, addStudentsDtoRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<Void> finishClass(@PathVariable Long id) {

        classService.finish(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
