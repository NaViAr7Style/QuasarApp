package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.dto.AddPublisherDTO;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidAddPublisherDTO;
import static bg.deliev.quasarapp.testUtils.TestUtils.createValidPublisher;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceImplTest {

  private final ModelMapper modelMapper = new ModelMapper();

  @Mock
  private PublisherRepository publisherRepository;

  @Mock
  private GameRepository gameRepository;

  private PublisherServiceImpl publisherService;

  @BeforeEach
  void setUp() {
    // Mix of real and mocked dependencies, so setup is necessary instead of @InjectMocks
    publisherService = new PublisherServiceImpl(publisherRepository, modelMapper, gameRepository);
  }

  @Test
  void testGetAllPublisherNames_ReturnsListOfNames() {
    PublisherEntity entity1 = createValidPublisher("Ubisoft");
    PublisherEntity entity2 = createValidPublisher("EA");

    when(publisherRepository.findAll()).thenReturn(List.of(entity1, entity2));

    List<String> names = publisherService.getAllPublisherNames();

    assertEquals(List.of("Ubisoft", "EA"), names);
  }

  @Test
  void testDeletePublisher_WhenNoGames_ShouldDelete() {
    PublisherEntity entity = createValidPublisher("Test Publisher");
    entity.setId(1L);

    when(publisherRepository.findById(1L)).thenReturn(Optional.of(entity));
    when(gameRepository.findAllByPublisherId(1L)).thenReturn(List.of());

    publisherService.deletePublisher(1L);

    verify(publisherRepository).deleteById(1L);
  }

  @Test
  void testDeletePublisher_WhenHasGames_ShouldThrow() {
    PublisherEntity entity = createValidPublisher("Test Publisher");
    entity.setId(1L);

    when(publisherRepository.findById(1L)).thenReturn(Optional.of(entity));
    when(gameRepository.findAllByPublisherId(1L)).thenReturn(List.of(new GameEntity()));

    UnsupportedOperationException ex = assertThrows(
        UnsupportedOperationException.class,
        () -> publisherService.deletePublisher(1L)
    );

    assertTrue(ex.getMessage().contains("has 1 published games"));
  }

  @Test
  void testGetPublisherName_WhenExists_ReturnsName() {
    PublisherEntity entity = createValidPublisher("Test Publisher");
    entity.setId(1L);

    when(publisherRepository.findById(1L)).thenReturn(Optional.of(entity));

    assertEquals("Test Publisher", publisherService.getPublisherName(1L));
  }

  @Test
  void testGetPublisherName_WhenNotExists_Throws() {
    when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

    NoSuchElementException ex = assertThrows(
        NoSuchElementException.class,
        () -> publisherService.getPublisherName(1L)
    );

    assertTrue(ex.getMessage().contains("doesn't exist"));
  }

  @Test
  void testAddPublisher_WhenNotExists_Saves() {
    AddPublisherDTO dto = createValidAddPublisherDTO("Test Publisher");

    when(publisherRepository.findByName("Test Publisher")).thenReturn(Optional.empty());

    publisherService.addPublisher(dto);

    verify(publisherRepository).save(
        argThat(publisher -> publisher.getName().equals("Test Publisher"))
    );
  }

  @Test
  void testAddPublisher_WhenExists_Throws() {
    AddPublisherDTO dto = createValidAddPublisherDTO("Test Publisher");

    when(publisherRepository.findByName("Test Publisher"))
        .thenReturn(Optional.of(new PublisherEntity()));

    assertThrows(EntityExistsException.class, () -> publisherService.addPublisher(dto));
  }
}
