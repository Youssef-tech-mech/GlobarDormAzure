package ntu.service_centric.global_dorm.controllers;

import ntu.service_centric.global_dorm.models.User;
import ntu.service_centric.global_dorm.services.ProfileService;
import ntu.service_centric.global_dorm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/page")
    public String showUserProfilePage(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login"; // Redirect to login if not authenticated
        }

        String email = principal.getName();
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "profile-page"; // Render profile-page.html from templates
        } else {
            return "redirect:/login"; // Redirect if user not found
        }
    }

    @PutMapping("/update-password")
    @ResponseBody
    public ResponseEntity<String> updatePassword(
            Principal principal,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword) {

        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
            }

            String email = principal.getName();
            Optional<User> user = userService.getUserByEmail(email);

            if (user.isPresent()) {
                User existingUser = user.get();

                // Validate current password
                if (!passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect.");
                }

                // Validate new password
                if (!isPasswordValid(newPassword)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            "Password must be at least 8 characters long, include 1 uppercase letter, 1 lowercase letter, and 1 digit."
                    );
                }

                // Update password
                profileService.updatePassword(existingUser.getUserID(), newPassword);
                return ResponseEntity.ok("Password updated successfully.");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            // Log the exception details
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/delete-account")
    @ResponseBody
    public String deleteAccount(Principal principal) {
        if (principal == null) {
            return "error: Unauthorized access.";
        }

        String email = principal.getName();
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            profileService.deleteUser(user.get().getUserID());
            return "success: Account deleted successfully.";
        }

        return "error: User not found.";
    }

    public boolean isPasswordValid(String password) {
        // Regex to enforce at least 1 digit, 1 uppercase letter, 1 lowercase letter, and minimum 8 characters
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password != null && password.matches(passwordPattern);
    }


    @PostMapping("/update-details")
    @ResponseBody
    public String updateUserDetails(
            Principal principal,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("role") String role) {

        if (principal == null) {
            return "error: Unauthorized access.";
        }

        String userEmail = principal.getName();
        Optional<User> user = userService.getUserByEmail(userEmail);

        if (user.isPresent()) {
            profileService.updateUserDetails(user.get().getUserID(), name, email, role);
            return "success: User details updated successfully.";
        }

        return "error: User not found.";
    }



}
