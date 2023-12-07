package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.AddGameDTO;
import bg.deliev.quasarapp.model.dto.AddPublisherDTO;
import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import bg.deliev.quasarapp.service.interfaces.GameService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/add")
    public ModelAndView add(@ModelAttribute("addGameDTO") AddGameDTO addGameDTO) {

        return new ModelAndView("add-game");
    }

    @PostMapping("/add")
    public ModelAndView add(@Valid AddGameDTO addGameDTO,
                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("add-publisher");
        }

        gameService.addGame(addGameDTO);

        return new ModelAndView("redirect:/games/all");
    }

}
