package bg.deliev.quasarapp.model.validation;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MatchesPatternValidatorTest {

  private MatchesPatternValidator validator;

  @BeforeEach
  void setUp() {
    validator = new MatchesPatternValidator();

    MatchesPattern annotation = new MatchesPattern() {
      @Override
      public String regex() {
        return "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).+$";
      }

      @Override
      public String message() {
        return "Must contain at least 1 uppercase, 1 lowercase, and 1 special character.";
      }

      @Override
      public Class<?>[] groups() {
        return new Class[0];
      }

      @Override
      public Class<? extends Payload>[] payload() {
        return new Class[0];
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return MatchesPattern.class;
      }
    };

    validator.initialize(annotation);
  }

  @Test
  void whenValidString_thenReturnsTrue() {
    assertTrue(validator.isValid("Password@", mock(ConstraintValidatorContext.class)));
    assertTrue(validator.isValid("A@1z", mock(ConstraintValidatorContext.class)));
  }

  @Test
  void whenInvalidString_thenReturnsFalse() {
    assertFalse(validator.isValid("password", mock(ConstraintValidatorContext.class))); // no uppercase, no special
    assertFalse(validator.isValid("PASSWORD", mock(ConstraintValidatorContext.class))); // no lowercase, no special
    assertFalse(validator.isValid("Password", mock(ConstraintValidatorContext.class))); // no special
    assertFalse(validator.isValid("password@", mock(ConstraintValidatorContext.class))); // no uppercase
  }

  @Test
  void whenNullString_thenReturnsFalse() {
    assertFalse(validator.isValid(null, mock(ConstraintValidatorContext.class)));
  }
}
