package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.model.dto.AddGameDTO;
import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import bg.deliev.quasarapp.model.dto.PublisherSummaryDTO;
import bg.deliev.quasarapp.model.enums.GameGenreEnum;
import bg.deliev.quasarapp.service.interfaces.GameService;
import bg.deliev.quasarapp.service.interfaces.PublisherService;
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

import java.util.Arrays;
import java.util.List;

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

    @GetMapping("/add")
    public ModelAndView add(@ModelAttribute("addGameDTO") AddGameDTO addGameDTO) {

        return new ModelAndView("add-game");
    }

    @PostMapping("/add")
    public ModelAndView add(@Valid AddGameDTO addGameDTO,
                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("add-game");
        }

        gameService.addGame(addGameDTO);

        return new ModelAndView("redirect:/games/all");
    }

    @ModelAttribute("genres")
    public List<String> genres() {
        return Arrays.stream(GameGenreEnum.values())
                .map(Enum::name)
                .toList();
    }

    @ModelAttribute("publishers")
    public List<String> publishers() {
        return publisherService
                .getAll()
                .stream()
                .map(PublisherSummaryDTO::getName)
                .sorted()
                .toList();
    }

}
