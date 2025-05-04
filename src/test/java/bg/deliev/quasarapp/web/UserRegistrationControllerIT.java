package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.UserRegistrationDTO;
import bg.deliev.quasarapp.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidUserRegistrationDTO;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserRegistrationControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  void testUsersRegisterReturnsRegisterView() throws Exception {
    mockMvc.perform(get("/users/register"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("userRegistrationDTO"));
  }

  @Test
  void testRegisterRedirectsToLoginOnSuccessfulRegistration() throws Exception {
    UserRegistrationDTO dto = createValidUserRegistrationDTO();

    when(userService.registerUser(dto)).thenReturn(true);

    mockMvc.perform(post("/users/register")
            .with(csrf())
            .flashAttr("userRegistrationDTO", dto))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/users/login"));
  }

  @Test
  void testRegisterReturnsToFormOnValidationErrors() throws Exception {
    mockMvc.perform(post("/users/register")
            .with(csrf())
            .param("email", "")
            .param("password", "")
            .param("confirmPassword", "")
        )
        .andExpect(status().isOk())
        .andExpect(view().name("register"));

    verify(userService, never()).registerUser(new UserRegistrationDTO());
  }

  @Test
  void testRegisterReturnsToFormWithErrorFlagIfRegistrationFailsWithValidInput() throws Exception {
    UserRegistrationDTO dto = createValidUserRegistrationDTO();

    when(userService.registerUser(dto)).thenReturn(false);

    mockMvc.perform(post("/users/register")
            .with(csrf())
            .flashAttr("userRegistrationDTO", dto)
        )
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("hasRegistrationError"));
  }
}