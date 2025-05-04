package bg.deliev.quasarapp.model.validation;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StringFieldMatchValidatorTest {

  record TestDTO(String password, String confirmPassword) {}

  private StringFieldMatchValidator validator;

  @BeforeEach
  void setUp() {
    validator = new StringFieldMatchValidator();

    StringFieldMatch annotation = new StringFieldMatch() {
      @Override
      public String first() {
        return "password";
      }

      @Override
      public String second() {
        return "confirmPassword";
      }

      @Override
      public String message() {
        return "Passwords must match";
      }

      @Override
      public Class<?>[] groups() {
        return new Class<?>[0];
      }

      @Override
      @SuppressWarnings("unchecked")
      public Class<? extends Payload>[] payload() {
        return new Class[0];
      }

      @Override
      public Class<StringFieldMatch> annotationType() {
        return StringFieldMatch.class;
      }
    };

    validator.initialize(annotation);
  }



  @Test
  void testWhenFieldsMatchValidationPasses() {
    TestDTO dto = new TestDTO("password123", "password123");

    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    assertTrue(validator.isValid(dto, context));
  }

  @Test
  void testWhenFieldsDoNotMatchValidationFails() {
    TestDTO dto = new TestDTO("password123", "differentPassword");

    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    ConstraintValidatorContext.ConstraintViolationBuilder builder
        = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

    ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder
        = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);

    when(context.buildConstraintViolationWithTemplate(anyString()))
        .thenReturn(builder);
    when(builder.addPropertyNode(anyString()))
        .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));
    when(nodeBuilder.addConstraintViolation())
        .thenReturn(context);

    assertFalse(validator.isValid(dto, context));
  }

  @Test
  void testWhenOneFieldIsNullValidationFails() {
    TestDTO dto = new TestDTO("password123", null);

    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
    ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

    when(context.buildConstraintViolationWithTemplate(anyString()))
        .thenReturn(builder);
    when(builder.addPropertyNode(anyString()))
        .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

    assertFalse(validator.isValid(dto, context));
  }

  @Test
  void testWhenObjectIsNullValidationFails() {
    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    assertFalse(validator.isValid(null, context));
  }
}