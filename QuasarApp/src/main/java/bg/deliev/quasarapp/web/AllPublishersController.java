package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.PublisherSummaryDTO;
import bg.deliev.quasarapp.service.interfaces.PublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

}
