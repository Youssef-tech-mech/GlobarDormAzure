package ntu.service_centric.global_dorm.controllers;

import ntu.service_centric.global_dorm.models.api.LoginRequest;
import ntu.service_centric.global_dorm.models.api.LoginResponse;
import ntu.service_centric.global_dorm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);
        return loginResponse != null
                ? ResponseEntity.ok(loginResponse)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
