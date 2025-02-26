package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.model.dto.ContactUsDTO;
import bg.deliev.quasarapp.service.interfaces.EmailService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/contacts")
public class ContactUsController {

    private final EmailService emailService;

    public ContactUsController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public ModelAndView contacts(@ModelAttribute("contactUsDTO") ContactUsDTO contactUsDTO) {
        return new ModelAndView("contacts");
    }

    @PostMapping
    public ModelAndView contacts(@Valid ContactUsDTO contactUsDTO,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("contacts");
        }

        emailService.sendFeedbackEmail(contactUsDTO);

        return new ModelAndView("redirect:/");
    }

}
