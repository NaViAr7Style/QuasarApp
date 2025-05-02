package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.dto.AddPublisherDTO;
import bg.deliev.quasarapp.model.dto.PublisherSummaryDTO;
import bg.deliev.quasarapp.model.entity.GameEntity;
import bg.deliev.quasarapp.model.entity.PublisherEntity;
import bg.deliev.quasarapp.repository.GameRepository;
import bg.deliev.quasarapp.repository.PublisherRepository;
import bg.deliev.quasarapp.service.interfaces.PublisherService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static bg.deliev.quasarapp.testUtils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PublisherServiceImplIT {

  @Autowired
  private PublisherRepository publisherRepository;

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  @SuppressWarnings("unused") // necessary dependency for PublisherServiceImpl and used inside it
  private ModelMapper modelMapper;

  @Autowired
  private PublisherService publisherService;

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
  void testAddPublisher_savesToRepository() {
    AddPublisherDTO dto = createValidAddPublisherDTO("Another Publisher");

    publisherService.addPublisher(dto);

    assertTrue(publisherRepository.findByName("Another Publisher").isPresent());
  }

  @Test
  void testAddPublisher_throwsExceptionWhenNameExists() {
    AddPublisherDTO dto = createValidAddPublisherDTO(TEST_PUBLISHER_NAME);

    // Depends on the test setup, where the publisher is already saved
    assertThrows(EntityExistsException.class, () -> publisherService.addPublisher(dto));
  }

  @Test
  void testGetAllPublishers_returnsPage() {
    Page<PublisherSummaryDTO> page = publisherService.getAllPublishers(PageRequest.of(0, 10));

    assertEquals(1, page.getTotalElements());
    assertEquals(TEST_PUBLISHER_NAME, page.getContent().get(0).getName());
  }

  @Test
  void testGetAllPublisherNames_returnsList() {
    List<String> names = publisherService.getAllPublisherNames();

    assertEquals(1, names.size());
    assertTrue(names.contains(TEST_PUBLISHER_NAME));
  }

  @Test
  void testGetPublisherName_returnsName() {
    String name = publisherService.getPublisherName(testPublisher.getId());

    assertEquals(TEST_PUBLISHER_NAME, name);
  }

  @Test
  void testGetPublisherName_throwsWhenInvalidId() {
    assertThrows(NoSuchElementException.class, () -> publisherService.getPublisherName(9999L));
  }

  @Test
  void testDeletePublisher_succeedsWhenNoGames() {
    publisherService.deletePublisher(testPublisher.getId());

    assertFalse(publisherRepository.findById(testPublisher.getId()).isPresent());
  }

  @Test
  void testDeletePublisher_throwsWhenGamesExist() {
    GameEntity game = createValidGame(testPublisher);
    gameRepository.save(game);

    assertThrows(UnsupportedOperationException.class, () -> publisherService.deletePublisher(testPublisher.getId()));
  }
}