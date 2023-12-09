package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.dto.AddPublisherDTO;
import bg.deliev.quasarapp.model.dto.PublisherSummaryDTO;
import bg.deliev.quasarapp.model.entity.GameEntity;
import bg.deliev.quasarapp.model.entity.PublisherEntity;
import bg.deliev.quasarapp.repository.GameRepository;
import bg.deliev.quasarapp.repository.PublisherRepository;
import bg.deliev.quasarapp.service.aop.WarnIfExecutionExceeds;
import bg.deliev.quasarapp.service.interfaces.PublisherService;
import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final ModelMapper modelMapper;
    private final GameRepository gameRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository,
                                ModelMapper modelMapper,
                                GameRepository gameRepository) {
        this.publisherRepository = publisherRepository;
        this.modelMapper = modelMapper;
        this.gameRepository = gameRepository;
    }

    @WarnIfExecutionExceeds(timeInMillis = 1000)
    @Override
    public Page<PublisherSummaryDTO> getAllPublishers(Pageable pageable) {
        return publisherRepository
                .findAll(pageable)
                .map(publisherEntity -> modelMapper.map(publisherEntity, PublisherSummaryDTO.class));
    }

    @Override
    public void deletePublisher(Long id) {

        PublisherEntity publisherEntity = validatePublisher(id);

        List<GameEntity> allByPublisherId = gameRepository.findAllByPublisherId(id);

        if (!allByPublisherId.isEmpty()) {
            throw new UnsupportedOperationException(
                    "Publisher " +
                    publisherEntity.getName() +
                    " has " + allByPublisherId.size() +
                    " published games!"
            );
        }

        publisherRepository.deleteById(id);
    }

    @Override
    public String getPublisherName(long id) {
        return validatePublisher(id).getName();
    }

    @Override
    public void addPublisher(AddPublisherDTO addPublisherDTO) {
        Optional<PublisherEntity> byName = publisherRepository.findByName(addPublisherDTO.getName());

        if (byName.isPresent()) {
            throw new EntityExistsException("Publisher already exists!");
        }

        PublisherEntity publisherEntity = modelMapper.map(addPublisherDTO, PublisherEntity.class);

        publisherRepository.save(publisherEntity);
    }

    @WarnIfExecutionExceeds(timeInMillis = 1000)
    @Override
    public List<PublisherSummaryDTO> getAll() {

        List<PublisherEntity> entities = publisherRepository.findAll();

        return entities
                .stream()
                .map(e -> modelMapper.map(e, PublisherSummaryDTO.class))
                .toList();
    }

    private PublisherEntity validatePublisher(long id) {
        Optional<PublisherEntity> optPublisher = publisherRepository.findById(id);

        if (optPublisher.isEmpty()) {
            throw new NoSuchElementException("Publisher with id " + id + " doesn't exist!");
        }

        return optPublisher.get();
    }

}
