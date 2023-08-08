package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.employee.DtoRequest.InstructorDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.InstructorDtoResponse;
import gabriel.moraes.school.Model.employee.Instructor;
import gabriel.moraes.school.repository.InstructorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final ModelMapper mapper;

    public InstructorService(InstructorRepository instructorRepository, ModelMapper mapper) {
        this.instructorRepository = instructorRepository;
        this.mapper = mapper;
    }


    public InstructorDtoResponse save(InstructorDtoRequest instructorDtoRequest) {
        Instructor instructor = mapper.map(instructorDtoRequest, Instructor.class);
        Instructor instructorSaved = instructorRepository.save(instructor);
        return mapper.map(instructorSaved, InstructorDtoResponse.class);
    }
}
