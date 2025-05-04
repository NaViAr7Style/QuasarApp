package bg.deliev.quasarapp.model.validation;

import bg.deliev.quasarapp.model.entity.PublisherEntity;
import bg.deliev.quasarapp.repository.PublisherRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UniquePublisherNameValidatorTest {

  @Mock
  private PublisherRepository publisherRepository;

  @Mock
  private ConstraintValidatorContext context;

  @InjectMocks
  private UniquePublisherNameValidator validator;

  @Test
  void testIsValidReturnsTrueWhenNameIsUnique() {
    String publisherName = "UniquePublisher";

    when(publisherRepository.findByName(publisherName)).thenReturn(Optional.empty());

    boolean result = validator.isValid(publisherName, context);

    assertTrue(result);
  }

  @Test
  void testIsValidReturnsFalseWhenNameAlreadyExists() {
    String publisherName = "ExistingPublisher";

    when(publisherRepository.findByName(publisherName)).thenReturn(Optional.of(new PublisherEntity()));

    boolean result = validator.isValid(publisherName, context);

    assertFalse(result);
  }

  @Test
  void testIsValidReturnsFalseWhenNameIsNull() {
    boolean result = validator.isValid(null, context);

    assertFalse(result);
  }
}
