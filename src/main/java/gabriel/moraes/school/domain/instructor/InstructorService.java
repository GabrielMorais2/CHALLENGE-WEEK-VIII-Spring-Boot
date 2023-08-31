package gabriel.moraes.school.domain.instructor;

import gabriel.moraes.school.domain.instructor.dto.InstructorDtoRequest;
import gabriel.moraes.school.domain.instructor.dto.InstructorDtoResponse;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
                .orElseThrow(() -> new ObjectNotFoundException("Instructor not found with id: " + id));

        return mapper.map(instructor, InstructorDtoResponse.class);
    }
    public List<InstructorDtoResponse> getAllInstructors() {
        List<Instructor> instructors = instructorRepository.findAll();
        return instructors.stream()
                .map(instructor -> mapper.map(instructor, InstructorDtoResponse.class))
                .collect(Collectors.toList());
    }
    @Transactional
    public InstructorDtoResponse save(InstructorDtoRequest instructorDtoRequest) {
        Instructor instructor = mapper.map(instructorDtoRequest, Instructor.class);
        Instructor instructorSaved = instructorRepository.save(instructor);
        return mapper.map(instructorSaved, InstructorDtoResponse.class);
    }

    public List<Instructor> getAllInstructorsById(List<Long> instructorIds) {
        List<Instructor> instructors = instructorRepository.findAllById(instructorIds);

        if (instructorIds.size() != instructors.size()) {
            List<Long> notFoundIds = new ArrayList<>(instructorIds);
            notFoundIds.removeAll(instructors.stream().map(Instructor::getId).toList());
            throw new ObjectNotFoundException("Instructors not found for IDs: " + notFoundIds);
        }

        return instructors;
    }
}
