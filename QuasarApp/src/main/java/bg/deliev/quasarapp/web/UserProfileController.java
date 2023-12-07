package bg.deliev.quasarapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserProfileController {

    @GetMapping("/user/{id}")
    public ModelAndView userProfile(@PathVariable String id) {

        return new ModelAndView("faq");
    }

}
