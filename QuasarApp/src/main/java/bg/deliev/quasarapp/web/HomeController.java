package bg.deliev.quasarapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/faq")
    public ModelAndView faq() {
        return new ModelAndView("faq");
    }

    @GetMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("about");
    }

    @GetMapping("/contacts")
    public ModelAndView contacts() {
        return new ModelAndView("contacts");
    }

}
