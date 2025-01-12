package ntu.service_centric.global_dorm.controllers;

import ntu.service_centric.global_dorm.models.Application;
import ntu.service_centric.global_dorm.models.User;
import ntu.service_centric.global_dorm.services.ApplicationService;
import ntu.service_centric.global_dorm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserService userService;

    /**
     * Render applications for the logged-in user.
     */
    @GetMapping
    public String showApplications(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isEmpty()) {
            return "redirect:/login";
        }

        int userID = user.get().getUserID();
        List<Application> applications = applicationService.getApplicationsByUserId(userID);
        model.addAttribute("applications", applications);
        model.addAttribute("user", user.get());
        return "applications";
    }

    /**
     * Render the application form for the Apply button.
     */
    @GetMapping("/form")
    public String showApplicationForm(Model model) {
        return "application-form";
    }

    /**
     * API: Get applications for the logged-in user.
     */
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Application>> getApplicationsForUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = principal.getName();
        int userID = userService.getUserByEmail(email)
                .map(User::getUserID)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        List<Application> applications = applicationService.getApplicationsByUserId(userID);
        return ResponseEntity.ok(applications);
    }

    /**
     * API: Submit a new application.
     */
    @PostMapping("/api/submit")
    public ResponseEntity<Application> submitApplication(
            @RequestParam int roomID,
            @RequestParam int userID) {
        Application application = applicationService.submitApplication(roomID, userID);
        return ResponseEntity.ok(application);
    }

    /**
     * API: Get applications for a specific room.
     */
    @GetMapping("/api/room/{roomID}")
    public ResponseEntity<List<Application>> getApplicationsByRoom(@PathVariable int roomID) {
        return ResponseEntity.ok(applicationService.getApplicationsByRoom(roomID));
    }

    /**
     * API: Get an application by its appID.
     */
    @GetMapping("/api/{appID}")
    public ResponseEntity<Application> getApplicationByAppID(@PathVariable int appID) {
        return applicationService.getApplicationByAppID(appID)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * API: Update the status of an application.
     */
    @PutMapping("/api/{appID}/status")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable int appID,
            @RequestBody Map<String, String> request) {
        return applicationService.updateApplicationStatus(appID, request.get("status"))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * API: Cancel an application by its appID.
     */
    @DeleteMapping("/api/{appID}/cancel")
    @ResponseBody
    public ResponseEntity<String> cancelApplication(@PathVariable int appID, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String email = principal.getName();
        int userID = userService.getUserByEmail(email)
                .map(User::getUserID)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        boolean isCancelled = applicationService.cancelApplication(appID, userID);
        if (isCancelled) {
            return ResponseEntity.ok("Application cancelled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel the application.");
        }
    }

    @RequestMapping("/api/applications")
    @PostMapping("/apply")
        public ResponseEntity<String> applyForRoom(
                @RequestParam int roomID,
                Principal principal) {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
            }

            String email = principal.getName();
            int userID = userService.getUserByEmail(email)
                    .map(User::getUserID)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            applicationService.submitApplication(roomID, userID);
            return ResponseEntity.ok("Application submitted successfully.");
        }



}
