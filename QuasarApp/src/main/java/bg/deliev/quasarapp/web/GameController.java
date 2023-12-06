package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.GameDetailsDTO;
import bg.deliev.quasarapp.service.interfaces.GameService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public ModelAndView details(@PathVariable("id") Long id) {

        GameDetailsDTO gameDetailsDTO = gameService.getGameDetails(id);

        ModelAndView modelAndView = new ModelAndView("game-details");

        modelAndView.addObject("game", gameDetailsDTO);

        return modelAndView;
    }

}
