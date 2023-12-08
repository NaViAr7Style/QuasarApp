package bg.deliev.quasarapp.web;

import bg.deliev.quasarapp.service.authentication.QuasarUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoleManagementController {

    @GetMapping("/users/manage-roles")
    public ModelAndView manageRoles(@AuthenticationPrincipal QuasarUserDetails userDetails) {

        ModelAndView modelAndView = new ModelAndView("role-management");

        modelAndView.addObject("username", userDetails.getUsername());

        return modelAndView;
    }

}
