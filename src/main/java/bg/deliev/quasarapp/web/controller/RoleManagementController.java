package bg.deliev.quasarapp.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoleManagementController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/manage-roles")
    public ModelAndView manageRoles(@AuthenticationPrincipal UserDetails userDetails) {

        ModelAndView modelAndView = new ModelAndView("role-management");

        modelAndView.addObject("username", userDetails.getUsername());

        return modelAndView;
    }

}
