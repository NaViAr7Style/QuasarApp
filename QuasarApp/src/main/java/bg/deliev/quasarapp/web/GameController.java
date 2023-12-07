package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.GameDetailsDTO;
import bg.deliev.quasarapp.service.interfaces.GameService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public ModelAndView details(@PathVariable("id") Long id, HttpServletRequest request) {

        GameDetailsDTO gameDetailsDTO = gameService.getGameDetails(id);
        String lastPage = request.getHeader("Referer");

        URI uri = UriComponentsBuilder.fromUriString(lastPage).build().toUri();

        String size = UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst("size");
        String page = UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst("page");

        ModelAndView modelAndView = new ModelAndView("game-details");

        modelAndView.addObject("game", gameDetailsDTO);
        modelAndView.addObject("size", size);
        modelAndView.addObject("page", page);

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {

        gameService.deleteOffer(id);

        return new ModelAndView("redirect:/games/all");
    }

}
