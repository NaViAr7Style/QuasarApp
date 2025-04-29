package bg.deliev.quasarapp.model.dto;

import bg.deliev.quasarapp.model.entity.PublisherEntity;
import bg.deliev.quasarapp.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidAddPublisherDTO;
import static bg.deliev.quasarapp.testUtils.TestUtils.createValidPublisher;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AddPublisherDTOValidationIT {

  @Autowired
  private Validator validator;

  @Autowired
  private PublisherRepository publisherRepository;

  @Test
  void shouldPassValidation_whenAllFieldsAreValidAndNameIsUnique() {
    AddPublisherDTO dto = createValidAddPublisherDTO();

    Set<ConstraintViolation<AddPublisherDTO>> violations = validator.validate(dto);

    assertThat(violations).isEmpty();
  }

  @Test
  void shouldFailValidation_whenPublisherNameAlreadyExists() {
    PublisherEntity existingPublisher = createValidPublisher();
    existingPublisher.setName("existingPublisher");
    publisherRepository.save(existingPublisher);

    AddPublisherDTO dto = new AddPublisherDTO();
    dto.setName("existingPublisher");
    dto.setThumbnailUrl("https://example.com/thumb.png");

    Set<ConstraintViolation<AddPublisherDTO>> violations = validator.validate(dto);

    assertThat(violations)
        .anyMatch(v -> v.getPropertyPath().toString().equals("name") &&
            v.getMessage().equals("Publisher already exists!"));
  }

  @Test
  void shouldFailValidation_whenFieldsAreEmpty() {
    AddPublisherDTO dto = new AddPublisherDTO();
    dto.setName("");
    dto.setThumbnailUrl("");

    Set<ConstraintViolation<AddPublisherDTO>> violations = validator.validate(dto);

    assertThat(violations).hasSize(2);
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("thumbnailUrl"));
  }
}