package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.AddGameDTO;
import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import bg.deliev.quasarapp.model.enums.GameGenreEnum;
import bg.deliev.quasarapp.service.interfaces.GameService;
import bg.deliev.quasarapp.service.interfaces.PublisherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidAddGameDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AllGamesControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private GameService gameService;

  @MockBean
  private PublisherService publisherService;

  @Test
  void testGetAllGames_ReturnsGamesView() throws Exception {
    Page<GameSummaryDTO> mockPage = new PageImpl<>(List.of(new GameSummaryDTO()));

    when(gameService.getAllGames(any(Pageable.class))).thenReturn(mockPage);

    mockMvc.perform(get("/games/all"))
        .andExpect(status().isOk())
        .andExpect(view().name("games"))
        .andExpect(model().attributeExists("games"));

    verify(gameService).getAllGames(any(Pageable.class));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testAddGame_ValidInput_Redirects() throws Exception {
    AddGameDTO addGameDTO = createValidAddGameDTO();

    mockMvc.perform(post("/games/add")
            .with(csrf())
            .flashAttr("addGameDTO", addGameDTO))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/games/all"));

    verify(gameService).addGame(any(AddGameDTO.class));
  }

  @Test
  @WithMockUser(roles = "USER")
  void testGetAddGameForm_AsUser_Forbidden() throws Exception {
    mockMvc.perform(get("/games/add"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testAddGame_InvalidInput_ShowsForm() throws Exception {
    AddGameDTO invalidDto = new AddGameDTO();

    invalidDto.setName("");
    invalidDto.setDescription("");
    invalidDto.setPrice(BigDecimal.valueOf(-10));
    invalidDto.setGenre(GameGenreEnum.ACTION);
    invalidDto.setThumbnailUrl("");
    invalidDto.setPublisherName("");

    when(publisherService.getAllPublisherNames()).thenReturn(List.of());

    mockMvc.perform(post("/games/add")
            .with(csrf())
            .flashAttr("addGameDTO", invalidDto))
        .andExpect(status().isOk())
        .andExpect(view().name("add-game"))
        .andExpect(model().attributeExists("genres"))
        .andExpect(model().attributeExists("publishers"))
        .andExpect(model().hasErrors());

    verify(gameService, never()).addGame(any());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testGetAddGameForm_AsAdmin_ReturnsForm() throws Exception {
    when(publisherService.getAllPublisherNames()).thenReturn(List.of());

    mockMvc.perform(get("/games/add"))
        .andExpect(status().isOk())
        .andExpect(view().name("add-game"))
        .andExpect(model().attributeExists("genres"))
        .andExpect(model().attributeExists("publishers"));
  }
}