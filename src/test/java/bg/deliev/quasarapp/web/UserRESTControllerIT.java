package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.UpdateUserRolesDTO;
import bg.deliev.quasarapp.model.dto.UserManagementDTO;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.service.interfaces.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidUpdateUserRolesDTO;
import static bg.deliev.quasarapp.testUtils.TestUtils.createValidUserManagementDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRESTControllerIT {

  // Test data - created in TestUtils with the below values
  private static final long TEST_USER_ID = 1L;
  private static final String TEST_EMAIL = "john_doe@test.com";
  private static final UserRoleEnum TEST_USER_ROLE = UserRoleEnum.USER;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testGetAllUsers() throws Exception {
    UserManagementDTO user = createValidUserManagementDTO();

    when(userService.getAllUsers()).thenReturn(List.of(user));

    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].email").value(TEST_EMAIL))
        .andExpect(jsonPath("$[0].roles[0]").value(TEST_USER_ROLE.name()));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testDeleteUserById() throws Exception {
    doNothing().when(userService).deleteUserById(TEST_USER_ID);

    mockMvc.perform(delete("/api/users/{id}", TEST_USER_ID)
            .with(csrf()))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testUpdateUserRoles_Success() throws Exception {
    UpdateUserRolesDTO updateDTO = createValidUpdateUserRolesDTO();

    UserManagementDTO updatedUser = createValidUserManagementDTO();

    when(userService.findUserById(TEST_USER_ID)).thenReturn(Optional.of(updatedUser));
    doNothing().when(userService).updateUserRoles(any(UpdateUserRolesDTO.class));

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(TEST_EMAIL))
        .andExpect(jsonPath("$.roles[0]").value(TEST_USER_ROLE.name()));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testUpdateUserRoles_UserNotFound() throws Exception {
    UpdateUserRolesDTO updateDTO = createValidUpdateUserRolesDTO();

    when(userService.findUserById(TEST_USER_ID)).thenReturn(Optional.empty());

    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO))
            .with(csrf()))
        .andExpect(status().isNotFound());
  }
}
