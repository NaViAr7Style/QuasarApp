package bg.deliev.quasarapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class AuthenticationController {

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

}
