package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.service.interfaces.GameService;
import bg.deliev.quasarapp.service.interfaces.PublisherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static bg.deliev.quasarapp.web.controller.PaginationUtils.extractPaginationParams;

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

        PaginationUtils.PaginationParams paginationParams = extractPaginationParams(request);

        ModelAndView modelAndView = new ModelAndView("publisher-details");

        modelAndView.addObject("publisherId", id);
        modelAndView.addObject("publisherName", publisherService.getPublisherName(id));
        modelAndView.addObject("games", gameService.getAllGamesByPublisherId(id, pageable));
        modelAndView.addObject("size", paginationParams.size());
        modelAndView.addObject("page", paginationParams.page());

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {

        publisherService.deletePublisher(id);

        return new ModelAndView("redirect:/publishers/all");
    }

}
