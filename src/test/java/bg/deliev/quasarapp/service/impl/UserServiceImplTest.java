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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidUserRegistrationDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private UserActivationCodeRepository userActivationCodeRepository;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void testRegisterUser_ShouldReturnTrue_WhenRegistrationSucceeds() {
    UserRegistrationDTO dto = createValidUserRegistrationDTO();
    dto.setPassword("password");

    UserEntity userEntity = new UserEntity();

    when(modelMapper.map(dto, UserEntity.class)).thenReturn(userEntity);
    when(passwordEncoder.encode("password")).thenReturn("encoded");
    when(roleRepository.getByRole(UserRoleEnum.USER)).thenReturn(new UserRoleEntity());

    boolean result = userService.registerUser(dto);

    assertThat(result).isTrue();
    verify(userRepository).save(any(UserEntity.class));
  }

  @Test
  void testRegisterUser_ShouldReturnFalse_WhenExceptionIsThrown() {
    UserRegistrationDTO dto = new UserRegistrationDTO();

    when(modelMapper.map(any(), any())).thenThrow(new RuntimeException("Mapping failed"));

    boolean result = userService.registerUser(dto);

    assertThat(result).isFalse();
  }

  @Test
  void testFindByUsername_ShouldReturnUserDetails_WhenUserExists() {
    String email = "user@example.com";

    UserEntity userEntity = new UserEntity();
    UserDetailsDTO dto = new UserDetailsDTO();

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
    when(modelMapper.map(userEntity, UserDetailsDTO.class)).thenReturn(dto);

    UserDetailsDTO result = userService.findByUsername(email);

    assertThat(result).isNotNull();
    verify(userRepository).findByEmail(email);
  }

  @Test
  void testFindByUsername_ShouldThrow_WhenUserNotFound() {
    String email = "unknown";

    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.findByUsername(email))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessageContaining("User with username " + email + " not found!");
  }

  @Test
  void testGetAllUsers_ShouldReturnAdminsFirstSortedByEmail() {
    UserEntity user1 = new UserEntity();
    user1.setId(1L);
    user1.setEmail("zuser@example.com");
    user1.setRoles(Set.of(new UserRoleEntity(UserRoleEnum.USER)));

    UserEntity user2 = new UserEntity();
    user2.setId(2L);
    user2.setEmail("zadmin@example.com");
    user2.setRoles(Set.of(new UserRoleEntity(UserRoleEnum.ADMIN)));

    UserEntity user3 = new UserEntity();
    user3.setId(3L);
    user3.setEmail("xadmin@example.com");
    user3.setRoles(Set.of(new UserRoleEntity(UserRoleEnum.ADMIN)));

    UserEntity user4 = new UserEntity();
    user4.setId(4L);
    user4.setEmail("xuser@example.com");
    user4.setRoles(Set.of(new UserRoleEntity(UserRoleEnum.USER)));

    UserManagementDTO dto1 = new UserManagementDTO();
    dto1.setId(1L);
    dto1.setEmail("zuser@example.com");

    UserManagementDTO dto2 = new UserManagementDTO();
    dto2.setId(2L);
    dto2.setEmail("zadmin@example.com");

    UserManagementDTO dto3 = new UserManagementDTO();
    dto3.setId(3L);
    dto3.setEmail("xadmin@example.com");

    UserManagementDTO dto4 = new UserManagementDTO();
    dto4.setId(4L);
    dto4.setEmail("xuser@example.com");

    when(userRepository.findAll()).thenReturn(List.of(user1, user2, user3, user4));
    when(modelMapper.map(user1, UserManagementDTO.class)).thenReturn(dto1);
    when(modelMapper.map(user2, UserManagementDTO.class)).thenReturn(dto2);
    when(modelMapper.map(user3, UserManagementDTO.class)).thenReturn(dto3);
    when(modelMapper.map(user4, UserManagementDTO.class)).thenReturn(dto4);

    List<UserManagementDTO> result = userService.getAllUsers();

    // Admins first (xadmin, zadmin), then users (xuser, zuser)
    assertThat(result).extracting("id").containsExactly(3L, 2L, 4L, 1L);
  }

  @Test
  void testFindUserById_ShouldReturnDTO_WhenUserExists() {
    UserEntity user = new UserEntity();
    UserManagementDTO dto = new UserManagementDTO();
    dto.setId(1L);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(modelMapper.map(user, UserManagementDTO.class)).thenReturn(dto);

    Optional<UserManagementDTO> result = userService.findUserById(1L);

    assertThat(result).isPresent().get().hasFieldOrPropertyWithValue("id", 1L);
  }

  @Test
  void testFindUserById_ShouldReturnEmpty_WhenUserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<UserManagementDTO> result = userService.findUserById(1L);

    assertThat(result).isEmpty();
  }

  @Test
  void testDeleteUserById_ShouldRemoveRolesAndDeleteUser() {
    Long userId = 1L;
    UserEntity user = new UserEntity();
    user.setId(userId);
    user.setRoles(Set.of(new UserRoleEntity(UserRoleEnum.USER)));

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userActivationCodeRepository.findAllByUserId(userId)).thenReturn(List.of());

    userService.deleteUserById(userId);

    assertThat(user.getRoles()).isEmpty();
    verify(userRepository).delete(user);
  }

  @Test
  void testDeleteUserById_ShouldThrow_WhenUserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.deleteUserById(1L))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("User not found");
  }

  @Test
  void testUpdateUserRoles_ShouldUpdateRolesSuccessfully() {
    UpdateUserRolesDTO dto = new UpdateUserRolesDTO();
    dto.setId(1L);
    dto.setRoles(List.of("USER", "ADMIN"));

    UserEntity user = new UserEntity();

    Set<UserRoleEntity> expectedRoles = Set.of(
        new UserRoleEntity(UserRoleEnum.USER),
        new UserRoleEntity(UserRoleEnum.ADMIN)
    );

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(roleRepository.findAllByRoleIn(Set.of(UserRoleEnum.USER, UserRoleEnum.ADMIN)))
        .thenReturn(expectedRoles);

    userService.updateUserRoles(dto);

    assertThat(user.getRoles()).containsExactlyInAnyOrderElementsOf(expectedRoles);
    verify(userRepository).save(user);
  }
}
