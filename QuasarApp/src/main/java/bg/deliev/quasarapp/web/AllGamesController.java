package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import bg.deliev.quasarapp.service.interfaces.GameService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/games")
public class AllGamesController {

    private final GameService gameService;

    public AllGamesController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/all")
    public ModelAndView all(@PageableDefault(size = 3, sort = "name") Pageable pageable) {

        Page<GameSummaryDTO> allGames = gameService.getAllGames(pageable);

        ModelAndView modelAndView = new ModelAndView("games");

        modelAndView.addObject("games", allGames);

        return modelAndView;
    }

}
