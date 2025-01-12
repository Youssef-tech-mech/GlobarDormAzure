package ntu.service_centric.global_dorm.services;

import ntu.service_centric.global_dorm.models.User;
import ntu.service_centric.global_dorm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CredentialService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    /**
     * Register a new user with email, password validation, and default role USER.
     */
    public User registerUser(String username, String password) {
        if (userRepository.findByEmail(username).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password must contain at least 1 digit, 1 uppercase, 1 lowercase letter, and be at least 8 characters long.");
        }

        User user = new User();
        user.setEmail(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        user.setName("Default Name");

        return userRepository.save(user);
    }

    /**
     * Validate the password against the criteria.
     */
    private boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }

    /**
     * Validate credentials for login.
     */
    public boolean validateCredentials(String username, String password) {
        return userRepository.findByEmail(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }
}
