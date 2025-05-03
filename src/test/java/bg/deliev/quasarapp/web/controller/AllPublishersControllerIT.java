package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.model.dto.AddPublisherDTO;
import bg.deliev.quasarapp.model.dto.PublisherSummaryDTO;
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

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidAddPublisherDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AllPublishersControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PublisherService publisherService;

  @Test
  void testGetAllPublishers_ReturnsPublishersView() throws Exception {
    Page<PublisherSummaryDTO> mockPage = new PageImpl<>(List.of(new PublisherSummaryDTO()));
    when(publisherService.getAllPublishers(any(Pageable.class))).thenReturn(mockPage);

    mockMvc.perform(get("/publishers/all"))
        .andExpect(status().isOk())
        .andExpect(view().name("publishers"))
        .andExpect(model().attributeExists("publishers"));

    verify(publisherService).getAllPublishers(any(Pageable.class));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testGetAddPublisherForm_AsAdmin_ReturnsForm() throws Exception {
    mockMvc.perform(get("/publishers/add"))
        .andExpect(status().isOk())
        .andExpect(view().name("add-publisher"))
        .andExpect(model().attributeExists("addPublisherDTO"));
  }

  @Test
  @WithMockUser(roles = "USER")
  void testGetAddPublisherForm_AsUser_Forbidden() throws Exception {
    mockMvc.perform(get("/publishers/add"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testAddPublisher_ValidInput_Redirects() throws Exception {
    AddPublisherDTO validDto = createValidAddPublisherDTO("Valid Publisher");

    mockMvc.perform(post("/publishers/add")
            .with(csrf())
            .flashAttr("addPublisherDTO", validDto))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/publishers/all"));

    verify(publisherService).addPublisher(any(AddPublisherDTO.class));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testAddPublisher_InvalidInput_ShowsFormWithErrors() throws Exception {
    AddPublisherDTO invalidDto = new AddPublisherDTO();
    invalidDto.setName(""); // Invalid: empty
    invalidDto.setThumbnailUrl(""); // Invalid: empty

    mockMvc.perform(post("/publishers/add")
            .with(csrf())
            .flashAttr("addPublisherDTO", invalidDto))
        .andExpect(status().isOk())
        .andExpect(view().name("add-publisher"))
        .andExpect(model().attributeHasFieldErrors("addPublisherDTO", "name", "thumbnailUrl"));

    verify(publisherService, never()).addPublisher(any());
  }
}