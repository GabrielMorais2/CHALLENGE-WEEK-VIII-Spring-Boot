package gabriel.moraes.school.domain.coordinator;

import gabriel.moraes.school.domain.coordinator.dto.CoordinatorDtoRequest;
import gabriel.moraes.school.domain.coordinator.dto.CoordinatorDtoResponse;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public CoordinatorDtoResponse getCoordinatorById(Long id) {
        Coordinator coordinator = coordinatorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Coordinator not found with id: " + id));

        return mapper.map(coordinator, CoordinatorDtoResponse.class);
    }

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

    public List<Coordinator> getAllCoordinatorsById(List<Long> coordinatorIds) {
        List<Coordinator> coordinators = coordinatorRepository.findAllById(coordinatorIds);

        if (coordinatorIds.size() != coordinators.size()) {
            List<Long> notFoundIds = new ArrayList<>(coordinatorIds);
            notFoundIds.removeAll(coordinators.stream().map(Coordinator::getId).toList());
            throw new ObjectNotFoundException("Coordinators not found for IDs: " + notFoundIds);
        }

        return coordinators;
    }
}
