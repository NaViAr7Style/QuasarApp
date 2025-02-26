package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.service.interfaces.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @PostMapping("/login-error")
    public ModelAndView loginError(@ModelAttribute("email") String email) {
        ModelAndView modelAndView = new ModelAndView("login");

        modelAndView.addObject("bad_credentials", true);

        return modelAndView;
    }

    @GetMapping("/activate")
    public ModelAndView verifyUser(@RequestParam("activation_code") String activationCode,
                                   @AuthenticationPrincipal UserDetails userDetails) {

        userService.verifyUser(activationCode);

        if (userDetails.getUsername() != null) {
            return new ModelAndView("redirect:/");
        }

        return new ModelAndView("login");
    }


}
