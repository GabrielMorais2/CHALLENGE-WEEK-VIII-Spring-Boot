package gabriel.moraes.school.controller;

import gabriel.moraes.school.Model.DtoRequest.ScrumMasterDtoRequest;
import gabriel.moraes.school.Model.DtoResponse.ScrumMasterDtoResponse;
import gabriel.moraes.school.service.ScrumMasterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scrum-masters")
public class ScrumMasterController {

    private final ScrumMasterService scrumMasterService;

    public ScrumMasterController(ScrumMasterService scrumMasterService) {
        this.scrumMasterService = scrumMasterService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ScrumMasterDtoResponse> getScrumMasterById(@PathVariable Long id) {
        ScrumMasterDtoResponse dto = scrumMasterService.getScrumMasterById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ScrumMasterDtoResponse>> getAllScrumMasters() {
        List<ScrumMasterDtoResponse> dtos = scrumMasterService.getAllScrumMasters();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ScrumMasterDtoResponse> save(@Valid @RequestBody ScrumMasterDtoRequest scrumMasterDtoRequest){
        ScrumMasterDtoResponse dto = scrumMasterService.save(scrumMasterDtoRequest);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
