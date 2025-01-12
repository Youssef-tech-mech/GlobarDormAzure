package ntu.service_centric.global_dorm.controllers;

import ntu.service_centric.global_dorm.models.User;
import ntu.service_centric.global_dorm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LandingController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboardPage(Principal principal, Model model) {
        if (principal == null || principal.getName() == null) {
            return "redirect:/login"; // Redirect to login if not authenticated
        }

        String email = principal.getName();
        User user = userService.getUserByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login"; // Redirect to login if user not found
        }

        model.addAttribute("user", user); // Add user details to the model
        return "dashboard"; // Render the dashboard.html view
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Login page
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // Register page
    }


}
