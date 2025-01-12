package ntu.service_centric.global_dorm.controllers;

import jakarta.validation.Valid;
import ntu.service_centric.global_dorm.models.User;
import ntu.service_centric.global_dorm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retrieve all users.
     *
     * @return List of all users.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieve a user by ID.
     *
     * @param userID The user ID.
     * @return User if found, otherwise 404 Not Found.
     */
    @GetMapping("/{userID}")
    public ResponseEntity<User> getUserById(@PathVariable int userID) {
        return userService.getUserById(userID)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieve a user by email.
     *
     * @param email The email of the user.
     * @return User if found, otherwise 404 Not Found.
     */
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new user.
     *
     * @param user The user details.
     * @return The created user or conflict response if email is already in use.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if (userService.emailExists(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use.");
        }
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Update an existing user.
     *
     * @param userID The user ID to update.
     * @param user   The updated user details.
     * @return Updated user if found, otherwise 404 Not Found.
     */
    @PutMapping("/{userID}")
    public ResponseEntity<User> updateUser(@PathVariable int userID, @Valid @RequestBody User user) {
        return userService.updateUser(userID, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a user by ID.
     *
     * @param userID The user ID to delete.
     * @return 204 No Content if deleted, otherwise 404 Not Found.
     */
    @DeleteMapping("/{userID}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userID) {
        if (userService.deleteUser(userID)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieve the profile of the logged-in user.
     */
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = principal.getName();
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Update the password of the logged-in user.
     */
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            Principal principal,
            @RequestBody Map<String, String> request) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String email = principal.getName();
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");

            // Validate password complexity
            if (!isPasswordValid(newPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Password must be at least 8 characters long, include 1 digit, 1 uppercase, and 1 lowercase letter.");
            }

            if (passwordEncoder.matches(currentPassword, user.get().getPassword())) {
                user.get().setPassword(passwordEncoder.encode(newPassword));
                userService.updateUser(user.get().getUserID(), user.get());
                return ResponseEntity.ok("Password updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    /**
     * Validate password complexity.
     */
    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password != null && password.matches(passwordPattern);
    }

    /**
     * Render the profile page.
     */
    @GetMapping("/profile-page")
    public String showUserProfilePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect to login if not authenticated
        }

        String email = principal.getName();
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isEmpty()) {
            return "redirect:/login"; // Redirect if the user is not found
        }

        model.addAttribute("user", user.get()); // Add user data to the model
        return "profile-page"; // Make sure profile-page.html exists in `templates`
    }

}
