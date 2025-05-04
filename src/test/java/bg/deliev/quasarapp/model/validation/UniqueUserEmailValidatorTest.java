package bg.deliev.quasarapp.model.validation;

import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.repository.UserRepository;
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
public class UniqueUserEmailValidatorTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private ConstraintValidatorContext context;

  @InjectMocks
  private UniqueUserEmailValidator validator;

  @Test
  void testIsValidReturnsTrueWhenNameIsUnique() {
    String username = "username@unique.com";

    when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

    boolean result = validator.isValid(username, context);

    assertTrue(result);
  }

  @Test
  void testIsValidReturnsFalseWhenNameAlreadyExists() {
    String username = "username@unique.com";

    when(userRepository.findByEmail(username)).thenReturn(Optional.of(new UserEntity()));

    boolean result = validator.isValid(username, context);

    assertFalse(result);
  }

  @Test
  void testIsValidReturnsFalseWhenNameIsNull() {
    boolean result = validator.isValid(null, context);

    assertFalse(result);
  }
}
