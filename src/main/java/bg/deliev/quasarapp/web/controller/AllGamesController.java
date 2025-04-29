package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.model.dto.AddGameDTO;
import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import bg.deliev.quasarapp.model.enums.GameGenreEnum;
import bg.deliev.quasarapp.service.interfaces.GameService;
import bg.deliev.quasarapp.service.interfaces.PublisherService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final PublisherService publisherService;

    public AllGamesController(GameService gameService,
                              PublisherService publisherService) {
        this.gameService = gameService;
        this.publisherService = publisherService;
    }

    @GetMapping("/all")
    public ModelAndView all(@PageableDefault(size = 3, sort = "name") Pageable pageable) {

        Page<GameSummaryDTO> allGames = gameService.getAllGames(pageable);

        ModelAndView modelAndView = new ModelAndView("games");

        modelAndView.addObject("games", allGames);

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public ModelAndView add(@ModelAttribute("addGameDTO") AddGameDTO addGameDTO) {

        ModelAndView modelAndView = new ModelAndView("add-game");

        modelAndView.addObject("genres", GameGenreEnum.values());

        modelAndView.addObject("publishers", publisherService.getAllPublisherNames());

        return modelAndView;
    }

    // TODO: Preserve the data in the form if there are errors as well as the Genre and Publisher list objects
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ModelAndView add(@Valid AddGameDTO addGameDTO,
                            BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView("add-game");

        if (bindingResult.hasErrors()) {

            modelAndView.addObject("genres", GameGenreEnum.values());
            modelAndView.addObject("publishers", publisherService.getAllPublisherNames());

            return modelAndView;
        }

        gameService.addGame(addGameDTO);

        return new ModelAndView("redirect:/games/all");
    }
}
