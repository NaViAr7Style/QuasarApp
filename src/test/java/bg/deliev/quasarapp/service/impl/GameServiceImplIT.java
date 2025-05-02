package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.dto.AddGameDTO;
import bg.deliev.quasarapp.model.dto.GameDetailsDTO;
import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import bg.deliev.quasarapp.model.entity.PublisherEntity;
import bg.deliev.quasarapp.repository.GameRepository;
import bg.deliev.quasarapp.repository.PublisherRepository;
import bg.deliev.quasarapp.service.interfaces.GameService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidAddGameDTO;
import static bg.deliev.quasarapp.testUtils.TestUtils.createValidPublisher;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GameServiceImplIT {

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private PublisherRepository publisherRepository;

  @Autowired
  @SuppressWarnings("unused") // necessary dependency for GameServiceImpl and used inside it
  private ModelMapper modelMapper;

  @Autowired
  private GameService gameService;

  private PublisherEntity testPublisher;
  private static final String TEST_PUBLISHER_NAME = "Test Publisher";
  private static final String TEST_GAME_NAME = "Test Game";

  @BeforeEach
  void setUp() {
    gameRepository.deleteAll();
    publisherRepository.deleteAll();

    testPublisher = createValidPublisher(TEST_PUBLISHER_NAME);
    publisherRepository.save(testPublisher);
  }

  @Test
  void testAddGame_success() {
    AddGameDTO game = createValidAddGameDTO(TEST_GAME_NAME, TEST_PUBLISHER_NAME);

    gameService.addGame(game);

    assertEquals(1, gameRepository.count());
    assertTrue(gameRepository.findByName(TEST_GAME_NAME).isPresent());
  }

  @Test
  void testAddGame_throwsWhenGameExists() {
    gameService.addGame(createValidAddGameDTO());

    assertThrows(EntityExistsException.class, () -> gameService.addGame(createValidAddGameDTO()));
  }

  @Test
  void testAddGame_throwsWhenPublisherNotFound() {
    AddGameDTO dto = createValidAddGameDTO();
    dto.setPublisherName("NonExistent");

    assertThrows(NoSuchElementException.class, () -> gameService.addGame(dto));
  }

  @Test
  void testGetAllGames_returnsPage() {
    gameService.addGame(createValidAddGameDTO(TEST_GAME_NAME, TEST_PUBLISHER_NAME));

    Page<GameSummaryDTO> page = gameService.getAllGames(PageRequest.of(0, 10));

    assertEquals(1, page.getTotalElements());
    assertEquals(TEST_GAME_NAME, page.getContent().get(0).getName());
  }

  @Test
  void testGetAllGamesByPublisherId_returnsPage() {
    gameService.addGame(createValidAddGameDTO(TEST_GAME_NAME, TEST_PUBLISHER_NAME));

    Page<GameSummaryDTO> page = gameService.getAllGamesByPublisherId(testPublisher.getId(), PageRequest.of(0, 10));

    assertEquals(1, page.getTotalElements());
    assertEquals(TEST_PUBLISHER_NAME, page.getContent().get(0).getPublisherName());
  }

  @Test
  void testGetGameDetails_success() {
    AddGameDTO dto = createValidAddGameDTO(TEST_GAME_NAME, TEST_PUBLISHER_NAME);
    gameService.addGame(dto);

    Long gameId = gameRepository.findByName(dto.getName()).orElseThrow().getId();
    GameDetailsDTO details = gameService.getGameDetails(gameId);

    assertEquals(dto.getName(), details.getName());
    assertEquals(dto.getPublisherName(), details.getPublisherName());
  }

  @Test
  void testGetGameDetails_throwsWhenNotFound() {
    assertThrows(NoSuchElementException.class, () -> gameService.getGameDetails(999L));
  }

  @Test
  void testDeleteGame_removesGame() {
    gameService.addGame(createValidAddGameDTO(TEST_GAME_NAME, TEST_PUBLISHER_NAME));

    Long gameId = gameRepository.findByName(TEST_GAME_NAME).orElseThrow().getId();
    gameService.deleteGame(gameId);

    assertFalse(gameRepository.findById(gameId).isPresent());
  }
}
