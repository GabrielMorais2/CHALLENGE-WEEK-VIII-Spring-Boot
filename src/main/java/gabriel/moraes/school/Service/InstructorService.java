package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.employee.DtoRequest.InstructorDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.InstructorDtoResponse;
import gabriel.moraes.school.Model.employee.Instructor;
import gabriel.moraes.school.repository.InstructorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final ModelMapper mapper;

    public InstructorService(InstructorRepository instructorRepository, ModelMapper mapper) {
        this.instructorRepository = instructorRepository;
        this.mapper = mapper;
    }

    public InstructorDtoResponse getInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found with id: " + id));

        return mapper.map(instructor, InstructorDtoResponse.class);
    }

    public List<InstructorDtoResponse> getAllInstructors() {
        List<Instructor> instructors = instructorRepository.findAll();
        return instructors.stream()
                .map(instructor -> mapper.map(instructor, InstructorDtoResponse.class))
                .collect(Collectors.toList());
    }

    public InstructorDtoResponse save(InstructorDtoRequest instructorDtoRequest) {
        Instructor instructor = mapper.map(instructorDtoRequest, Instructor.class);
        Instructor instructorSaved = instructorRepository.save(instructor);
        return mapper.map(instructorSaved, InstructorDtoResponse.class);
    }
}
