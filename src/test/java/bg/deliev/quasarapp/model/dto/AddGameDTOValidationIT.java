package bg.deliev.quasarapp.model.dto;

import bg.deliev.quasarapp.model.entity.GameEntity;
import bg.deliev.quasarapp.model.entity.PublisherEntity;
import bg.deliev.quasarapp.repository.GameRepository;
import bg.deliev.quasarapp.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Set;

import static bg.deliev.quasarapp.testUtils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AddGameDTOValidationIT {

  @Autowired
  private Validator validator;

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private PublisherRepository publisherRepository;

  @Test
  void shouldPassValidation_whenAllFieldsAreValidAndGameNameIsUnique() {
      AddGameDTO dto = createValidAddGameDTO();

      Set<ConstraintViolation<AddGameDTO>> violations = validator.validate(dto);

      assertThat(violations).isEmpty();
  }

  @Test
  void shouldFailValidation_whenGameNameAlreadyExists() {
    PublisherEntity publisher = createValidPublisher();
    publisherRepository.save(publisher);

    GameEntity existingGame = createValidGame(publisher);
    existingGame.setName("ExistingGame");
    gameRepository.save(existingGame);

    AddGameDTO dto = createValidAddGameDTO();
    dto.setName("ExistingGame");

    Set<ConstraintViolation<AddGameDTO>> violations = validator.validate(dto);

    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals("name") &&
            v.getMessage().equals("Game already exists!")
    );
  }

  @Test
  void shouldFailValidation_whenFieldsAreMissingOrInvalid() {
    AddGameDTO dto = new AddGameDTO();

    dto.setName("");
    dto.setDescription("");
    dto.setPrice(BigDecimal.valueOf(-10));
    dto.setGenre(null);
    dto.setThumbnailUrl("");
    dto.setPublisherName("");

    Set<ConstraintViolation<AddGameDTO>> violations = validator.validate(dto);

    assertThat(violations).hasSize(6);
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("price"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("genre"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("thumbnailUrl"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("publisherName"));
  }


}
