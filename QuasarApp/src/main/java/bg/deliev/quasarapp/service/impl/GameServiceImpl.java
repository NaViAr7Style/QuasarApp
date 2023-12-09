package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.dto.AddGameDTO;
import bg.deliev.quasarapp.model.dto.GameDetailsDTO;
import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import bg.deliev.quasarapp.model.entity.GameEntity;
import bg.deliev.quasarapp.model.entity.PublisherEntity;
import bg.deliev.quasarapp.repository.GameRepository;
import bg.deliev.quasarapp.repository.PublisherRepository;
import bg.deliev.quasarapp.service.aop.WarnIfExecutionExceeds;
import bg.deliev.quasarapp.service.interfaces.GameService;
import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;
    private final PublisherRepository publisherRepository;

    public GameServiceImpl(GameRepository gameRepository,
                           ModelMapper modelMapper,
                           PublisherRepository publisherRepository) {
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
        this.publisherRepository = publisherRepository;
    }

    @WarnIfExecutionExceeds(timeInMillis = 1000)
    @Override
    public Page<GameSummaryDTO> getAllGames(Pageable pageable) {
        return gameRepository
                .findAll(pageable)
                .map(gameEntity -> modelMapper.map(gameEntity, GameSummaryDTO.class));
    }

    @WarnIfExecutionExceeds(timeInMillis = 1000)
    @Override
    public Page<GameSummaryDTO> getAllGamesByPublisherId(long publisherId, Pageable pageable) {
        return gameRepository
                .findAllByPublisherId(publisherId, pageable)
                .map(gameEntity -> modelMapper.map(gameEntity, GameSummaryDTO.class));
    }

    @Override
    public GameDetailsDTO getGameDetails(Long id) {

        GameEntity gameEntity = gameRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Game with ID + " + id + " was not found"));

        return modelMapper.map(gameEntity, GameDetailsDTO.class);
    }

    @Override
    public void deleteOffer(Long id) {
        gameRepository.deleteById(id);
    }

    @Override
    public void addGame(AddGameDTO addGameDTO) {

        Optional<GameEntity> byName = gameRepository.findByName(addGameDTO.getName());

        if (byName.isPresent()) {
            throw new EntityExistsException("Game already exists!");
        }

        GameEntity gameEntity = modelMapper.map(addGameDTO, GameEntity.class);

        PublisherEntity publisherEntity = publisherRepository
                .findByName(addGameDTO.getPublisherName())
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "Publisher " +
                                addGameDTO.getPublisherName() +
                                " not found"
                        )
                );

        gameEntity.setPublisher(publisherEntity);

        gameRepository.save(gameEntity);
    }
}
