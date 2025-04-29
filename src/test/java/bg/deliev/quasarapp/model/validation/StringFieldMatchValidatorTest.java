package bg.deliev.quasarapp.model.validation;

import jakarta.validation.ConstraintValidatorContext;
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
      public Class<? extends jakarta.validation.Payload>[] payload() {
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
  void whenFieldsMatch_thenValidationPasses() {
    TestDTO dto = new TestDTO("password123", "password123");

    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    assertTrue(validator.isValid(dto, context));
  }

  @Test
  void whenFieldsDoNotMatch_thenValidationFails() {
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
  void whenOneFieldIsNull_thenValidationFails() {
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
  void whenObjectIsNull_thenValidationFails() {
    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    assertFalse(validator.isValid(null, context));
  }
}