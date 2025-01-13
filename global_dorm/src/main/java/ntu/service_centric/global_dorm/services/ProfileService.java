package ntu.service_centric.global_dorm.services;

import ntu.service_centric.global_dorm.models.User;
import ntu.service_centric.global_dorm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Fetch user details by user ID
    public User getUserDetailsByID(int userID) {
        return userRepository.findByUserID(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userID));
    }

    // Fetch user details by email
    public User getUserDetailsByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    // Update user details (only non-sensitive fields)
    public User updateUserDetails(int userID, String name, String email, String role) {
        return userRepository.findByUserID(userID).map(existingUser -> {
            existingUser.setName(name);
            existingUser.setEmail(email);
            existingUser.setRole(role);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userID));
    }

    // Update user password only
    public User updatePassword(int userID, String newPassword) {
        return userRepository.findByUserID(userID).map(existingUser -> {
            System.out.println("Updating password for user: " + existingUser.getEmail());
            existingUser.setPassword(passwordEncoder.encode(newPassword));
            User updatedUser = userRepository.save(existingUser);
            System.out.println("Password updated successfully for user: " + updatedUser.getEmail());
            return updatedUser;
        }).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userID));
    }


    // Delete user by ID
    public void deleteUser(int userID) {
        User user = userRepository.findByUserID(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userID));
        userRepository.delete(user);
    }

    // Custom exception for user-related errors
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public boolean isPasswordValid(String password) {
        // Define the password regex
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password != null && password.matches(passwordPattern);
    }

}
