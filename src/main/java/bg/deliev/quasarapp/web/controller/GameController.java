package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.service.interfaces.GameService;
import bg.deliev.quasarapp.util.PaginationUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static bg.deliev.quasarapp.util.PaginationUtils.extractPaginationParams;

@Controller
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public ModelAndView details(@PathVariable("id") Long id, HttpServletRequest request) {

        PaginationUtils.PaginationParams paginationParams = extractPaginationParams(request);

        ModelAndView modelAndView = new ModelAndView("game-details");

        modelAndView.addObject("game", gameService.getGameDetails(id));
        modelAndView.addObject("size", paginationParams.size());
        modelAndView.addObject("page", paginationParams.page());

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {

        gameService.deleteGame(id);

        return new ModelAndView("redirect:/games/all");
    }
}
