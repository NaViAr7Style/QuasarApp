package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.model.dto.GameDetailsDTO;
import bg.deliev.quasarapp.service.interfaces.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private GameService gameService;

  @Test
  void testGameDetails_ReturnsGameDetailsView() throws Exception {
    long gameId = 1L;
    String mockReferer = "http://localhost/games/all?size=10&page=0";

    when(gameService.getGameDetails(gameId)).thenReturn(new GameDetailsDTO());

    mockMvc.perform(get("/game/{id}", gameId)
            .header("Referer", mockReferer))
        .andExpect(status().isOk())
        .andExpect(view().name("game-details"))
        .andExpect(model().attributeExists("game"))
        .andExpect(model().attributeExists("size"))
        .andExpect(model().attributeExists("page"));

    verify(gameService).getGameDetails(gameId);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testDeleteGame_AsAdmin_Redirects() throws Exception {
    long gameId = 1L;

    mockMvc.perform(delete("/game/{id}", gameId)
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/games/all"));

    verify(gameService).deleteGame(gameId);
  }

  @Test
  @WithMockUser(roles = "USER")
  void testDeleteGame_AsUser_Forbidden() throws Exception {
    long gameId = 1L;

    mockMvc.perform(delete("/game/{id}", gameId)
            .with(csrf()))
        .andExpect(status().isForbidden());

    verify(gameService, never()).deleteGame(anyLong());
  }
}
