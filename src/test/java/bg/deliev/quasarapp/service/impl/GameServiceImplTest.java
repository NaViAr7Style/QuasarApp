package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.dto.AddGameDTO;
import bg.deliev.quasarapp.model.dto.GameDetailsDTO;
import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import bg.deliev.quasarapp.model.entity.GameEntity;
import bg.deliev.quasarapp.model.entity.PublisherEntity;
import bg.deliev.quasarapp.repository.GameRepository;
import bg.deliev.quasarapp.repository.PublisherRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

  private final ModelMapper modelMapper = new ModelMapper();

  @Mock
  private GameRepository gameRepository;

  @Mock
  private PublisherRepository publisherRepository;

  private GameServiceImpl gameService;

  @BeforeEach
  void setUp() {
    // Mix of real and mocked dependencies, so setup is necessary instead of @InjectMocks
    gameService = new GameServiceImpl(gameRepository, modelMapper, publisherRepository);
  }

  @Test
  void testGetAllGamesReturnsMappedDTOs() {
    GameEntity entity = new GameEntity();
    entity.setName("Halo");

    when(gameRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(entity)));

    Page<GameSummaryDTO> result = gameService.getAllGames(Pageable.unpaged());

    assertEquals(1, result.getContent().size());
    assertEquals("Halo", result.getContent().get(0).getName());
  }

  @Test
  void testGetAllGamesByPublisherIdReturnsMappedDTOs() {
    GameEntity entity = new GameEntity();
    entity.setName("Halo");

    when(gameRepository.findAllByPublisherId(eq(5L), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(entity)));

    Page<GameSummaryDTO> result = gameService.getAllGamesByPublisherId(5L, Pageable.unpaged());

    assertEquals("Halo", result.getContent().get(0).getName());
  }

  @Test
  void testGetGameDetailsReturnsDTO() {
    GameEntity game = new GameEntity();
    game.setName("Half-Life");

    when(gameRepository.findById(42L)).thenReturn(Optional.of(game));

    GameDetailsDTO dto = gameService.getGameDetails(42L);
    assertEquals("Half-Life", dto.getName());
  }

  @Test
  void testGetGameDetailsThrowsWhenNotFound() {
    when(gameRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> gameService.getGameDetails(1L));
  }

  @Test
  void testDeleteGameCallsRepository() {
    gameService.deleteGame(7L);
    verify(gameRepository).deleteById(7L);
  }

  @Test
  void testAddGameThrowsIfAlreadyExists() {
    AddGameDTO dto = new AddGameDTO();
    dto.setName("Portal");

    when(gameRepository.findByName("Portal")).thenReturn(Optional.of(new GameEntity()));

    assertThrows(EntityExistsException.class, () -> gameService.addGame(dto));
  }

  @Test
  void testAddGameSavesWhenValid() {
    AddGameDTO dto = new AddGameDTO();
    dto.setName("Portal");
    dto.setPublisherName("Valve");

    when(gameRepository.findByName("Portal")).thenReturn(Optional.empty());

    PublisherEntity publisher = new PublisherEntity();
    publisher.setName("Valve");

    when(publisherRepository.findByName("Valve")).thenReturn(Optional.of(publisher));

    gameService.addGame(dto);

    verify(gameRepository).save(any(GameEntity.class));
  }

  @Test
  void testAddGameThrowsWhenPublisherNotFound() {
    AddGameDTO dto = new AddGameDTO();
    dto.setName("Doom");
    dto.setPublisherName("id Software");

    when(gameRepository.findByName("Doom")).thenReturn(Optional.empty());
    when(publisherRepository.findByName("id Software")).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> gameService.addGame(dto));
  }
}