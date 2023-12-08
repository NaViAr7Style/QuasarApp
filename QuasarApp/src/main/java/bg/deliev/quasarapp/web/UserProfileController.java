package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.model.dto.UserDetailsDTO;
import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.service.interfaces.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/profile")
    public ModelAndView userProfile(@AuthenticationPrincipal UserDetails user) {

        UserDetailsDTO currentUser = null;

        if (user.getUsername() != null) {
            currentUser = userService.findByUsername(user.getUsername());
        }

        ModelAndView modelAndView = new ModelAndView("user-profile");

        modelAndView.addObject("user", currentUser);

        return modelAndView;
    }

}
