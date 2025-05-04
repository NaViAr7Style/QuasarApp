package bg.deliev.quasarapp.model.dto;

import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidUser;
import static bg.deliev.quasarapp.testUtils.TestUtils.createValidUserRegistrationDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRegistrationDTOValidationIT {

  @Autowired
  private Validator validator;

  @Autowired
  private UserRepository userRepository;

  @Test
  void testWhenValidHasNoViolations() {
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
  void testWhenFieldsAreEmptyHasValidationErrors() {
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
  void testWhenEmailIsInvalidHasValidationError() {
    UserRegistrationDTO dto = createValidUserRegistrationDTO();

    dto.setEmail("invalid-email");

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertTrue(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("email")
            && v.getMessage().contains("legitimate email address")));
  }

  @Test
  void testWhenPasswordIsTooShortHasValidationError() {
    UserRegistrationDTO dto = createValidUserRegistrationDTO();

    dto.setPassword("Short1!");
    dto.setConfirmPassword("Short1!");

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertTrue(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("password")
            && v.getMessage().contains("between 8 and 20 characters")));
  }

  @Test
  void testWhenPasswordDoesNotMatchPatternHasValidationError() {
    UserRegistrationDTO dto = createValidUserRegistrationDTO();

    dto.setPassword("lowercasepassword"); // missing uppercase and special characters
    dto.setConfirmPassword("lowercasepassword");

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertTrue(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("password")
            && v.getMessage().contains("Password must contain at least 1 uppercase")));
  }

  @Test
  void testWhenPasswordsDoNotMatchHasValidationError() {
    UserRegistrationDTO dto = createValidUserRegistrationDTO();

    dto.setPassword("Strong@1234");
    dto.setConfirmPassword("Different@1234");

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertTrue(violations.stream()
        .anyMatch(v -> v.getMessage().equals("Passwords should match.")));
  }

  @Test
  void testWhenEmailAlreadyExistsHasValidationError() {
    UserEntity validUser = createValidUser();
    validUser.setEmail("existing_email@test.com");
    userRepository.save(validUser);

    UserRegistrationDTO dto = createValidUserRegistrationDTO();
    dto.setEmail("existing_email@test.com");

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertTrue(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("email")
            && v.getMessage().equals("Empty email or already in use")));

  }

}