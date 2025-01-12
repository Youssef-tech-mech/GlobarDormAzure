package ntu.service_centric.global_dorm.controllers;

import jakarta.validation.Valid;
import ntu.service_centric.global_dorm.models.User;
import ntu.service_centric.global_dorm.services.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/credentials")
public class CredentialController {

    @Autowired
    private CredentialService credentialService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        try {
            User user = credentialService.registerUser(
                    request.get("username"),
                    request.get("password"));
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        boolean isValid = credentialService.validateCredentials(
                request.get("username"),
                request.get("password"));
        if (isValid) {
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful.",
                    "redirectUrl", "/dashboard" // Include the dashboard URL in the response
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials."));
        }
    }



}
