package ntu.service_centric.global_dorm.services;

import ntu.service_centric.global_dorm.models.User;
import ntu.service_centric.global_dorm.models.api.LoginRequest;
import ntu.service_centric.global_dorm.models.api.LoginResponse;
import ntu.service_centric.global_dorm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Retrieve all users.
     *
     * @return List of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieve a user by ID.
     *
     * @param id User ID.
     * @return User if found, otherwise null.
     */
    public User getUserById(String id) {
        try {
            Long userId = Long.parseLong(id);
            Optional<User> user = userRepository.findById(userId);
            return user.orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Create a new user.
     *
     * @param user User to create.
     * @return The created user.
     */
    public User createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password before saving
        return userRepository.save(user);
    }

    /**
     * Update an existing user.
     *
     * @param id          User ID.
     * @param updatedUser User object with updated details.
     * @return Updated user if found, otherwise null.
     */
    public User updateUser(String id, User updatedUser) {
        try {
            Long userId = Long.parseLong(id);
            return userRepository.findById(userId).map(existingUser -> {
                existingUser.setName(updatedUser.getName());
                existingUser.setEmail(updatedUser.getEmail());
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Update password if provided
                }
                existingUser.setRole(updatedUser.getRole());
                return userRepository.save(existingUser);
            }).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Delete a user by ID.
     *
     * @param id User ID.
     * @return true if the user was deleted, false otherwise.
     */
    public boolean deleteUser(String id) {
        try {
            Long userId = Long.parseLong(id);
            if (userRepository.existsById(userId)) {
                userRepository.deleteById(userId);
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        System.out.println("LoginRequest object: " + loginRequest);
        System.out.println("Email from request: " + loginRequest.getEmail());
        System.out.println("Password from request: " + loginRequest.getPassword());

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String token = generateToken(user);
                return new LoginResponse(user.getEmail(), user.getRole(), token);
            }
        }
        return null;
    }

    private String generateToken(User user) {
        // Placeholder token generation (use JWT or a secure mechanism in production)
        return user.getEmail() + "-auth-token";
    }
}


