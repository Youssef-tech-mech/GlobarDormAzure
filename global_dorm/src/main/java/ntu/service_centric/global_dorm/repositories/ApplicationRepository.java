package ntu.service_centric.global_dorm.repositories;

import ntu.service_centric.global_dorm.models.Application;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ApplicationRepository {

    private final Map<String, Application> applicationStore = new ConcurrentHashMap<>();
    private long idCounter = 1;

    // Retrieve all applications
    public List<Application> findAll() {
        return new ArrayList<>(applicationStore.values());
    }

    // Find an application by its ID
    public Optional<Application> findById(String id) {
        return Optional.ofNullable(applicationStore.get(id));
    }

    // Save a new application
    public Application save(Application application) {
        if (application.getId() == null || application.getId().isEmpty()) {
            String generatedId = String.valueOf(idCounter++);
            application.setId(generatedId);
        }
        applicationStore.put(application.getId(), application);
        return application;
    }

    // Update an existing application
    public Optional<Application> update(String id, Application updatedApplication) {
        if (applicationStore.containsKey(id)) {
            updatedApplication.setId(id);
            applicationStore.put(id, updatedApplication);
            return Optional.of(updatedApplication);
        }
        return Optional.empty();
    }

    // Delete an application by its ID
    public boolean deleteById(String id) {
        return applicationStore.remove(id) != null;
    }
}
