package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.employee.Coordinator;
import gabriel.moraes.school.Model.employee.DtoRequest.CoordinatorDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.CoordinatorDtoResponse;
import gabriel.moraes.school.repository.CoordinatorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoordinatorService {

    private final CoordinatorRepository coordinatorRepository;
    private final ModelMapper mapper;

    public CoordinatorService(CoordinatorRepository coordinatorRepository, ModelMapper mapper) {
        this.coordinatorRepository = coordinatorRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public CoordinatorDtoResponse getCoordinatorById(Long id) {
        Coordinator coordinator = coordinatorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coordinator not found with id: " + id));

        return mapper.map(coordinator, CoordinatorDtoResponse.class);
    }

    @Transactional(readOnly = true)
    public List<CoordinatorDtoResponse> getAllCoordinators() {
        List<Coordinator> coordinators = coordinatorRepository.findAll();
        return coordinators.stream()
                .map(coordinator -> mapper.map(coordinator, CoordinatorDtoResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public CoordinatorDtoResponse save(CoordinatorDtoRequest coordinatorDtoRequest) {
        Coordinator coordinator = mapper.map(coordinatorDtoRequest, Coordinator.class);
        Coordinator coordinatorSaved = coordinatorRepository.save(coordinator);
        return mapper.map(coordinatorSaved, CoordinatorDtoResponse.class);
    }
}
