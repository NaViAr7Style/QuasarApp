package bg.deliev.quasarapp.model.validation;

import bg.deliev.quasarapp.model.entity.GameEntity;
import bg.deliev.quasarapp.repository.GameRepository;
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
class UniqueGameNameValidatorTest {

  @Mock
  private GameRepository gameRepository;

  @Mock
  private ConstraintValidatorContext context;

  @InjectMocks
  private UniqueGameNameValidator validator;

  @Test
  void testIsValidReturnsTrueWhenNameIsUnique() {
    String gameName = "UniqueGame";

    when(gameRepository.findByName(gameName)).thenReturn(Optional.empty());

    boolean result = validator.isValid(gameName, context);

    assertTrue(result);
  }

  @Test
  void testIsValidReturnsFalseWhenNameAlreadyExists() {
    String gameName = "ExistingGame";

    when(gameRepository.findByName(gameName)).thenReturn(Optional.of(new GameEntity()));

    boolean result = validator.isValid(gameName, context);

    assertFalse(result);
  }

  @Test
  void testIsValidReturnsFalseWhenNameIsNull() {
    boolean result = validator.isValid(null, context);

    assertFalse(result);
  }
}
