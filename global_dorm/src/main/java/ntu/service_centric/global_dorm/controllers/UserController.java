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





}