package bg.deliev.quasarapp.init;

import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserRolesInitTest {

  private RoleRepository roleRepository;
  private UserRolesInit userRolesInit;

  @BeforeEach
  void setUp() {
    roleRepository = mock(RoleRepository.class);
    userRolesInit = new UserRolesInit(roleRepository);
  }

  @Test
  void run_shouldSeedRoles_whenRepositoryIsEmpty() {
    when(roleRepository.count()).thenReturn(0L);

    userRolesInit.run();

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<UserRoleEntity>> captor = ArgumentCaptor.forClass(List.class);
    verify(roleRepository).saveAll(captor.capture());

    List<UserRoleEntity> savedRoles = captor.getValue();
    assertEquals(UserRoleEnum.values().length, savedRoles.size());

    for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
      assertTrue(savedRoles.stream().anyMatch(r -> r.getRole() == roleEnum));
    }
  }

  @Test
  void run_shouldDoNothing_whenRolesExist() {
    when(roleRepository.count()).thenReturn(5L);

    userRolesInit.run();

    verify(roleRepository, never()).saveAll(any());
  }
}
