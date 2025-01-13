package ntu.service_centric.global_dorm.services;

import ntu.service_centric.global_dorm.models.User;
import ntu.service_centric.global_dorm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retrieve all users from the database.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieve a user by their userID (int).
     */
    public Optional<User> getUserById(int userID) {
        return userRepository.findByUserID(userID);
    }

    /**
     * Retrieve a user by their email address.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Check if an email is already in use.
     */
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Create a new user with email uniqueness check, password hashing, and auto-generated userID.
     */
    public User createUser(User user) {
        if (emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        // Generate the smallest available userID
        int smallestAvailableUserID = generateSmallestAvailableUserID();
        user.setUserID(smallestAvailableUserID);

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Generate the smallest available userID based on existing users.
     */
    private int generateSmallestAvailableUserID() {
        List<Integer> usedUserIDs = userRepository.findAll().stream()
                .map(User::getUserID)
                .sorted()
                .toList();

        return IntStream.range(1, Integer.MAX_VALUE)
                .filter(id -> !usedUserIDs.contains(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No available user IDs."));
    }



}