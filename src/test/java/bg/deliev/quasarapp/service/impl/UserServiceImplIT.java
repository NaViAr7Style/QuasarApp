package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.dto.UpdateUserRolesDTO;
import bg.deliev.quasarapp.model.dto.UserDetailsDTO;
import bg.deliev.quasarapp.model.dto.UserManagementDTO;
import bg.deliev.quasarapp.model.dto.UserRegistrationDTO;
import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.repository.RoleRepository;
import bg.deliev.quasarapp.repository.UserActivationCodeRepository;
import bg.deliev.quasarapp.repository.UserRepository;
import bg.deliev.quasarapp.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidUser;
import static bg.deliev.quasarapp.testUtils.TestUtils.createValidUserRegistrationDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplIT {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  @SuppressWarnings("unused") // necessary dependency for UserServiceImpl and used inside it
  private UserActivationCodeRepository userActivationCodeRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  @SuppressWarnings("unused") // necessary dependency for UserServiceImpl and used inside it
  private ModelMapper modelMapper;

  @Autowired
  private UserService userService;

  // Test data - created in TestUtils with the below values
  private static final String TEST_FIRST_NAME = "John";
  private static final String TEST_LAST_NAME = "Doe";
  private static final String TEST_EMAIL = "john_doe@test.com";
  private static final String TEST_PASSWORD = "Strong@1234";

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    roleRepository.deleteAll();
    userActivationCodeRepository.deleteAll();

    roleRepository.save(new UserRoleEntity(UserRoleEnum.USER));
    roleRepository.save(new UserRoleEntity(UserRoleEnum.ADMIN));
  }

  @Test
  void testRegisterUser_success() {
    UserRegistrationDTO dto = createValidUserRegistrationDTO();

    boolean result = userService.registerUser(dto);

    assertTrue(result);

    Optional<UserEntity> saved = userRepository.findByEmail(TEST_EMAIL);
    assertTrue(saved.isPresent());
    assertEquals(TEST_FIRST_NAME, saved.get().getFirstName());
    assertTrue(passwordEncoder.matches(TEST_PASSWORD, saved.get().getPassword()));
  }

  @Test
  void testRegisterUser_emailTaken() {
    UserEntity existingUser = createValidUser();
    existingUser.setRoles(Set.of(roleRepository.getByRole(UserRoleEnum.USER)));

    userRepository.save(existingUser);

    UserRegistrationDTO dto = createValidUserRegistrationDTO();

    boolean result = userService.registerUser(dto);

    assertFalse(result);
    assertEquals(1, userRepository.count());
  }

  @Test
  void testFindByUsername_success() {
    UserEntity user = createValidUser();
    user.setRoles(Set.of(roleRepository.getByRole(UserRoleEnum.USER)));

    userRepository.save(user);

    UserDetailsDTO dto = userService.findByUsername(TEST_EMAIL);

    assertNotNull(dto);
    assertEquals(TEST_EMAIL, dto.getEmail());
    assertTrue(dto.getRoles().contains("USER"));
  }

  @Test
  void testFindByUsername_throws() {
    assertThrows(RuntimeException.class, () -> userService.findByUsername("nonexistent@example.com"));
  }

  @Test
  void testGetAllUsers_returnsSortedByRoleAndEmail() {
    UserRoleEntity userRole = roleRepository.getByRole(UserRoleEnum.USER);
    UserRoleEntity adminRole = roleRepository.getByRole(UserRoleEnum.ADMIN);

    UserEntity user1 = createValidUser();
    user1.setEmail("xuser@example.com");
    user1.setRoles(Set.of(userRole));

    UserEntity user2 = createValidUser();
    user2.setEmail("zuser@example.com");
    user2.setRoles(Set.of(userRole));

    UserEntity user3 = createValidUser();
    user3.setEmail("zadmin@example.com");
    user3.setRoles(Set.of(adminRole, userRole));

    UserEntity user4 = createValidUser();
    user4.setEmail("xadmin@example.com");
    user4.setRoles(Set.of(adminRole, userRole));

    userRepository.save(user1);
    userRepository.save(user2);
    userRepository.save(user3);
    userRepository.save(user4);

    List<UserManagementDTO> result = userService.getAllUsers();

    assertEquals(4, result.size());

    // Admins first (xadmin, zadmin), then users (xuser, zuser)
    assertThat(result)
        .extracting("email")
        .containsExactly("xadmin@example.com", "zadmin@example.com", "xuser@example.com", "zuser@example.com");
  }

  @Test
  void testUpdateUserRoles_changesRoles() {
    UserEntity user = createValidUser();
    user.setRoles(Set.of(roleRepository.getByRole(UserRoleEnum.USER)));
    userRepository.save(user);

    UpdateUserRolesDTO dto = new UpdateUserRolesDTO();
    dto.setId(user.getId());
    dto.setRoles(List.of("ADMIN", "USER"));

    userService.updateUserRoles(dto);

    UserEntity updatedUser = userRepository.findById(user.getId()).orElseThrow();

    Set<UserRoleEntity> roles = updatedUser.getRoles();

    assertEquals(2, roles.size());
    assertTrue(roles.stream().anyMatch(r -> r.getRole() == UserRoleEnum.ADMIN) &&
              roles.stream().anyMatch(r -> r.getRole() == UserRoleEnum.USER));
  }

  @Test
  void testDeleteUserById_removesUser() {
    UserEntity user = createValidUser();
    user.setRoles(Set.of(roleRepository.getByRole(UserRoleEnum.USER)));
    userRepository.save(user);

    Long id = user.getId();
    assertTrue(userRepository.findById(id).isPresent());

    userService.deleteUserById(id);

    assertTrue(userRepository.findById(id).isEmpty());
  }
}
