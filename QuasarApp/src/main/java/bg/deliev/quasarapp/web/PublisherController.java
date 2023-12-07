package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import bg.deliev.quasarapp.service.interfaces.GameService;
import bg.deliev.quasarapp.service.interfaces.PublisherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("/publisher")
public class PublisherController {

    private final PublisherService publisherService;
    private final GameService gameService;

    public PublisherController(PublisherService publisherService,
                               GameService gameService) {
        this.publisherService = publisherService;
        this.gameService = gameService;
    }

    @GetMapping("/{id}/games")
    public ModelAndView publisherGames(@PageableDefault(size = 3, sort = "name") Pageable pageable,
                                       @PathVariable long id,
                                       HttpServletRequest request) {

        Page<GameSummaryDTO> publisherGames = gameService.getAllGamesByPublisherId(id, pageable);

        ModelAndView modelAndView = new ModelAndView("publisher-games");

        modelAndView.addObject("games", publisherGames);
        modelAndView.addObject("publisherId", id);

        String publisherName = publisherService.getPublisherName(id);

        modelAndView.addObject("publisherName", publisherName);

        String lastPage = request.getHeader("Referer");

        URI uri = UriComponentsBuilder.fromUriString(lastPage).build().toUri();

        String size = UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst("size");
        String page = UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst("page");

        modelAndView.addObject("size", size);
        modelAndView.addObject("page", page);

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/games")
    public ModelAndView delete(@PathVariable("id") Long id) {

        publisherService.deletePublisher(id);

        return new ModelAndView("redirect:/publishers/all");
    }

}
