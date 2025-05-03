package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PublisherControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PublisherService publisherService;

  @MockBean
  private GameService gameService;

  @Test
  void testPublisherGames_ReturnsPublisherDetailsView() throws Exception {
    long publisherId = 1L;
    String mockReferer = "http://localhost/games/all?size=10&page=0";
    Page<GameSummaryDTO> mockPage = new PageImpl<>(List.of(new GameSummaryDTO()));

    when(publisherService.getPublisherName(publisherId)).thenReturn("Test Publisher");
    when(gameService.getAllGamesByPublisherId(eq(publisherId), any(Pageable.class))).thenReturn(mockPage);

    mockMvc.perform(get("/publisher/{id}/games", publisherId)
            .header("Referer", mockReferer))
        .andExpect(status().isOk())
        .andExpect(view().name("publisher-details"))
        .andExpect(model().attributeExists("publisherId"))
        .andExpect(model().attributeExists("publisherName"))
        .andExpect(model().attributeExists("games"))
        .andExpect(model().attributeExists("size"))
        .andExpect(model().attributeExists("page"));

    verify(publisherService).getPublisherName(publisherId);
    verify(gameService).getAllGamesByPublisherId(eq(publisherId), any(Pageable.class));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testDeletePublisher_AsAdmin_Redirects() throws Exception {
    long publisherId = 1L;

    mockMvc.perform(delete("/publisher/{id}", publisherId)
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/publishers/all"));

    verify(publisherService).deletePublisher(publisherId);
  }

  @Test
  @WithMockUser(roles = "USER")
  void testDeletePublisher_AsUser_Forbidden() throws Exception {
    long publisherId = 1L;

    mockMvc.perform(delete("/publisher/{id}", publisherId)
            .with(csrf()))
        .andExpect(status().isForbidden());

    verify(publisherService, never()).deletePublisher(anyLong());
  }
}
