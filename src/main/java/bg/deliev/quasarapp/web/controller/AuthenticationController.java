package bg.deliev.quasarapp.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class AuthenticationController {

//    private final UserService userService;

//    public AuthenticationController(UserService userService) {
//        this.userService = userService;
//    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/login-error")
    public ModelAndView loginError(@ModelAttribute("email") String email) {
        ModelAndView modelAndView = new ModelAndView("login");

        modelAndView.addObject("bad_credentials", true);

        return modelAndView;
    }

//    Unnecessary after the removal of account activation

//    @GetMapping("/activate")
//    public ModelAndView verifyUser(@RequestParam("activation_code") String activationCode,
//                                   @AuthenticationPrincipal UserDetails userDetails) {
//
//        userService.verifyUser(activationCode);
//
//        if (userDetails.getUsername() != null) {
//            return new ModelAndView("redirect:/");
//        }
//
//        return new ModelAndView("login");
//    }
}
