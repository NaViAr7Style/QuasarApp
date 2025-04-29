package bg.deliev.quasarapp.model.dto;

import bg.deliev.quasarapp.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserRegistrationDTOTest {

  @Autowired
  private Validator validator;

  @MockBean
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());
  }

  @Test
  void whenValidUserRegistrationDTO_thenNoViolations() {
    UserRegistrationDTO dto = new UserRegistrationDTO();

    dto.setFirstName("John");
    dto.setLastName("Doe");
    dto.setEmail("john.doe@example.com");
    dto.setPassword("Strong@1234");
    dto.setConfirmPassword("Strong@1234");

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertTrue(violations.isEmpty(), "There should be no validation errors");
  }

  @Test
  void whenFieldsAreEmpty_thenValidationErrors() {
    UserRegistrationDTO dto = new UserRegistrationDTO();

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    System.out.println("Validation errors:");
    violations.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);

    // The violations are 7 because:
    // 1. We have 1 jakarta validation constraint failing for each of the 5 fields,
    //    and it stops checking the rest of the jakarta validation constraints after the first one has failed
    // 2. We have 1 custom validation constraint failing for password and email fields
    // 3. We have 1 custom validation constraint failing for the confirmPassword field
    assertEquals(8, violations.size(), "Should have 8 violations for empty fields");
  }

  @Test
  void whenEmailIsInvalid_thenValidationError() {
    UserRegistrationDTO dto = createValidDTO();

    dto.setEmail("invalid-email");

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertTrue(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("email")
            && v.getMessage().contains("legitimate email address")));
  }

  @Test
  void whenPasswordIsTooShort_thenValidationError() {
    UserRegistrationDTO dto = createValidDTO();

    dto.setPassword("Short1!");
    dto.setConfirmPassword("Short1!");

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertTrue(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("password")
            && v.getMessage().contains("between 8 and 20 characters")));
  }

  @Test
  void whenPasswordDoesNotMatchPattern_thenValidationError() {
    UserRegistrationDTO dto = createValidDTO();

    dto.setPassword("lowercasepassword"); // missing uppercase and special characters
    dto.setConfirmPassword("lowercasepassword");

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertTrue(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("password")
            && v.getMessage().contains("Password must contain at least 1 uppercase")));
  }

  @Test
  void whenPasswordsDoNotMatch_thenValidationError() {
    UserRegistrationDTO dto = createValidDTO();

    dto.setPassword("Strong@1234");
    dto.setConfirmPassword("Different@1234");

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertTrue(violations.stream()
        .anyMatch(v -> v.getMessage().equals("Passwords should match.")));
  }

  private UserRegistrationDTO createValidDTO() {
    UserRegistrationDTO dto = new UserRegistrationDTO();

    dto.setFirstName("Jane");
    dto.setLastName("Doe");
    dto.setEmail("jane.doe@example.com");
    dto.setPassword("Strong@1234");
    dto.setConfirmPassword("Strong@1234");

    return dto;
  }
}