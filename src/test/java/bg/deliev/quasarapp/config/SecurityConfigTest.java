package bg.deliev.quasarapp.config;

import bg.deliev.quasarapp.model.dto.UpdateUserRolesDTO;
import bg.deliev.quasarapp.model.dto.UserDetailsDTO;
import bg.deliev.quasarapp.model.dto.UserManagementDTO;
import bg.deliev.quasarapp.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  void testPublicPathsAreAccessible() throws Exception {
    mockMvc.perform(get("/games/all")).andExpect(status().isOk());
    mockMvc.perform(get("/publishers/all")).andExpect(status().isOk());
    mockMvc.perform(get("/users/login")).andExpect(status().isOk());
    mockMvc.perform(get("/users/login-error")).andExpect(status().isOk());
    mockMvc.perform(get("/contacts")).andExpect(status().isOk());
    mockMvc.perform(get("/faq")).andExpect(status().isOk());
    mockMvc.perform(get("/about")).andExpect(status().isOk());
    mockMvc.perform(get("/users/register")).andExpect(status().isOk());
  }

  @Test
  void testRestrictedPathsRequireAuthentication() throws Exception {
    mockMvc.perform(get("/user/profile"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/users/login"));
  }

  @Test
  @WithMockUser(username = "user")
  void testAuthenticatedUserCanAccessProfile() throws Exception {
    
    when(userService.findByUsername("user")).thenReturn(new UserDetailsDTO());

    mockMvc.perform(get("/user/profile"))
        .andExpect(status().isOk());
  }

  @Test
  void testAdminPathsRequireAuthentication() throws Exception {
    mockMvc.perform(get("/games/add"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/users/login"));

    mockMvc.perform(get("/publishers/add"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/users/login"));

    mockMvc.perform(delete("/game/1").with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/users/login"));

    mockMvc.perform(get("/users/manage-roles"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/users/login"));

    mockMvc.perform(get("/api/users"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/users/login"));

    mockMvc.perform(delete("/api/users/1")
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/users/login"));

    mockMvc.perform(patch("/api/users")
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/users/login"));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testAdminCanAccessAdminEndpoints() throws Exception {
    mockMvc.perform(get("/games/add")).andExpect(status().isOk());
    mockMvc.perform(get("/publishers/add")).andExpect(status().isOk());
    mockMvc.perform(delete("/game/1").with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/games/all"));
    mockMvc.perform(get("/users/manage-roles")).andExpect(status().isOk());
    mockMvc.perform(get("/api/users")).andExpect(status().isOk());
    mockMvc.perform(delete("/api/users/1").with(csrf())).andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testAdminCanAccessAdminEndpointsWithPatchMethod() throws Exception {

    long userId = 1L;
    UpdateUserRolesDTO dto = new UpdateUserRolesDTO();
    dto.setId(userId);
    dto.setRoles(List.of("ROLE_USER"));

    UserManagementDTO returnedUser = new UserManagementDTO();
    returnedUser.setId(userId);
    returnedUser.setRoles(List.of("ROLE_USER"));

    when(userService.findUserById(userId)).thenReturn(Optional.of(returnedUser));
    doNothing().when(userService).updateUserRoles(any(UpdateUserRolesDTO.class));

    mockMvc.perform(patch("/api/users").with(csrf())
            .contentType("application/json")
            .content("{\"id\":1,\"roles\":[\"ROLE_USER\"]}"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user")
  void testUserCannotAccessAdminEndpoints() throws Exception {
    mockMvc.perform(get("/games/add")).andExpect(status().isForbidden());
    mockMvc.perform(get("/publishers/add")).andExpect(status().isForbidden());
    mockMvc.perform(delete("/game/1")).andExpect(status().isForbidden());
    mockMvc.perform(get("/users/manage-roles")).andExpect(status().isForbidden());
    mockMvc.perform(get("/api/users")).andExpect(status().isForbidden());
    mockMvc.perform(delete("/api/users/1")).andExpect(status().isForbidden());
    mockMvc.perform(patch("/api/users")
            .contentType("application/json")
            .content("{\"id\":1,\"roles\":[\"ROLE_USER\"]}"))
        .andExpect(status().isForbidden());
  }
}
