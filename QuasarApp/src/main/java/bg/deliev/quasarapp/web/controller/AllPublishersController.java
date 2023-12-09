package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.model.dto.AddPublisherDTO;
import bg.deliev.quasarapp.model.dto.PublisherSummaryDTO;
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

@Controller
@RequestMapping("/publishers")
public class AllPublishersController {

    private final PublisherService publisherService;

    public AllPublishersController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping("/all")
    public ModelAndView all(@PageableDefault(size = 3, sort = "name") Pageable pageable) {

        Page<PublisherSummaryDTO> allPublishers = publisherService.getAllPublishers(pageable);

        ModelAndView modelAndView = new ModelAndView("publishers");

        modelAndView.addObject("publishers", allPublishers);

        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView add(@ModelAttribute("addPublisherDTO") AddPublisherDTO addPublisherDTO) {

        return new ModelAndView("add-publisher");
    }

    @PostMapping("/add")
    public ModelAndView add(@Valid AddPublisherDTO addPublisherDTO,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("add-publisher");
        }

        publisherService.addPublisher(addPublisherDTO);

        return new ModelAndView("redirect:/publishers/all");
    }

}
