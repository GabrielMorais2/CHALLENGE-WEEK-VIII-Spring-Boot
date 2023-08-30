package gabriel.moraes.school.domain.scrummaster;

import gabriel.moraes.school.domain.scrummaster.dto.ScrumMasterDtoRequest;
import gabriel.moraes.school.domain.scrummaster.dto.ScrumMasterDtoResponse;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrumMasterService {

    private final ScrumMasterRepository scrumMasterRepository;
    private final ModelMapper mapper;

    public ScrumMasterService(ScrumMasterRepository scrumMasterRepository, ModelMapper mapper) {
        this.scrumMasterRepository = scrumMasterRepository;
        this.mapper = mapper;
    }
    @Transactional(readOnly = true)
    public ScrumMasterDtoResponse getScrumMasterById(Long id) {
        ScrumMaster scrumMaster = scrumMasterRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Scrum Master not found with id: " + id));

        return mapper.map(scrumMaster, ScrumMasterDtoResponse.class);
    }
    @Transactional(readOnly = true)
    public List<ScrumMasterDtoResponse> getAllScrumMasters() {
        List<ScrumMaster> scrumMasters = scrumMasterRepository.findAll();
        return scrumMasters.stream()
                .map(scrumMaster -> mapper.map(scrumMaster, ScrumMasterDtoResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public ScrumMasterDtoResponse save(ScrumMasterDtoRequest scrumMasterDtoRequest) {
        ScrumMaster scrumMaster = mapper.map(scrumMasterDtoRequest, ScrumMaster.class);
        ScrumMaster scrumMasterSaved = scrumMasterRepository.save(scrumMaster);
        return mapper.map(scrumMasterSaved, ScrumMasterDtoResponse.class);
    }

    public List<ScrumMaster> getAllScrumMastersById(List<Long> scrumMasterIds) {
        List<ScrumMaster> scrumMasters = scrumMasterRepository.findAllById(scrumMasterIds);

        if (scrumMasterIds.size() != scrumMasters.size()) {
            List<Long> notFoundIds = new ArrayList<>(scrumMasterIds);
            notFoundIds.removeAll(scrumMasters.stream().map(ScrumMaster::getId).toList());
            throw new ObjectNotFoundException("Scrum Masters not found for IDs: " + notFoundIds);
        }
        return scrumMasters;
    }
}
