package ntu.service_centric.global_dorm.services;

import ntu.service_centric.global_dorm.models.Application;
import ntu.service_centric.global_dorm.repositories.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    @Autowired
    private ApplicationRepository applicationRepository;

    /**
     * Get all applications by user ID.
     */
    public List<Application> getApplicationsByUserId(int userID) {
        return applicationRepository.findByUserID(userID);
    }

    /**
     * Submit a new application with auto-generated appID.
     */
    public Application submitApplication(int roomID, int userID) {
        Application application = new Application();
        application.setAppID(generateNextAppID());
        application.setRoomID(roomID);
        application.setUserID(userID);
        application.setApplicationDate(LocalDateTime.now());
        application.setStatus("Pending");
        return applicationRepository.save(application);
    }

    /**
     * Generate the next available appID.
     */
    private int generateNextAppID() {
        return applicationRepository.findAll().stream()
                .map(Application::getAppID)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    /**
     * Get an application by its appID.
     */
    public Optional<Application> getApplicationByAppID(int appID) {
        return applicationRepository.findByAppID(appID);
    }

    /**
     * Get all applications for a specific room.
     */
    public List<Application> getApplicationsByRoom(int roomID) {
        return applicationRepository.findByRoomID(roomID);
    }

    /**
     * Update application status.
     */
    public Optional<Application> updateApplicationStatus(int appID, String status) {
        return applicationRepository.findByAppID(appID).map(application -> {
            application.setStatus(status);
            return applicationRepository.save(application);
        });
    }

    /**
     * Cancel an application by its appID.
     */
    public boolean cancelApplication(int appID, int userID) {
        // Find the application by appID
        Optional<Application> optionalApp = applicationRepository.findByAppID(appID);

        if (optionalApp.isPresent()) {
            Application application = optionalApp.get();

            // Check if the user owns the application
            if (application.getUserID() == userID) {
                applicationRepository.delete(application);
                return true; // Application canceled successfully
            }
        }

        return false; // Application not found or unauthorized
    }

}
