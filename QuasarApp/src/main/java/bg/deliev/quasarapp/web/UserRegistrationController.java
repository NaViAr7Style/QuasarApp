package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.UserRegistrationDTO;
import bg.deliev.quasarapp.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserRegistrationController {

    private final UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("register")
    public ModelAndView register(
            @ModelAttribute("userRegistrationDTO") UserRegistrationDTO userRegistrationDTO
    ) {
        return new ModelAndView("register");
    }

    @PostMapping("register")
    public ModelAndView register(
            @ModelAttribute("userRegistrationDTO") @Valid UserRegistrationDTO userRegistrationDTO,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }

        boolean isSuccessfullyRegistered = userService.registerUser(userRegistrationDTO);

        if (!isSuccessfullyRegistered) {
            ModelAndView modelAndView = new ModelAndView("register");

            modelAndView.addObject("hasRegistrationError", true);

            return modelAndView;
        }

        return new ModelAndView("redirect:/users/login");
    }

}
