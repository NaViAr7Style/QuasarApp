package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.model.dto.UserDetailsDTO;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidUserDetailsDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserProfileControllerIT {

  // Test data - created in TestUtils with the below values
  private static final String TEST_FIRST_NAME = "John";
  private static final String TEST_LAST_NAME = "Doe";
  private static final String TEST_EMAIL = "john_doe@test.com";
  private static final UserRoleEnum TEST_USER_ROLE = UserRoleEnum.USER;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  @WithMockUser(username = TEST_EMAIL)
  void testGetUserProfileReturnsViewWithFullUserDetails() throws Exception {
    UserDetailsDTO mockUser = createValidUserDetailsDTO();

    when(userService.findByUsername(TEST_EMAIL)).thenReturn(mockUser);

    MvcResult result = mockMvc.perform(get("/user/profile"))
        .andExpect(status().isOk())
        .andExpect(view().name("user-profile"))
        .andExpect(model().attributeExists("user"))
        .andReturn();

    UserDetailsDTO returnedUser =
        (UserDetailsDTO) Objects.requireNonNull(result.getModelAndView()).getModel().get("user");

    assertThat(returnedUser.getFirstName()).isEqualTo(TEST_FIRST_NAME);
    assertThat(returnedUser.getLastName()).isEqualTo(TEST_LAST_NAME);
    assertThat(returnedUser.getEmail()).isEqualTo(TEST_EMAIL);
    assertThat(returnedUser.getRoles()).containsExactly(TEST_USER_ROLE.toString());
  }

  @Test
  @WithMockUser
  void testGetUserProfileHandlesEmptyUsername() throws Exception {
    mockMvc.perform(get("/user/profile"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/login"));
  }

  @Test
  @WithMockUser(username = "unknownUser")
  void testGetUserProfileHandlesUserNotFound() throws Exception {
    when(userService.findByUsername("unknownUser")).thenReturn(null);

    mockMvc.perform(get("/user/profile"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/login"));
  }

  @Test
  void testGetUserProfileRejectsUnauthorizedAccess() throws Exception {
    mockMvc.perform(get("/user/profile"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/login"));
  }
}
