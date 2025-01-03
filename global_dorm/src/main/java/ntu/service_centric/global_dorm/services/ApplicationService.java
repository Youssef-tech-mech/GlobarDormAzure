package ntu.service_centric.global_dorm.services;

import ntu.service_centric.global_dorm.models.Application;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ApplicationService {

    private final Map<String, Application> applicationRepository = new ConcurrentHashMap<>();
    private long idCounter = 1;

    public List<Application> getAllApplications() {
        return new ArrayList<>(applicationRepository.values());
    }

    public Application getApplicationById(String id) {
        return applicationRepository.get(id);
    }

    public Application createApplication(Application application) {
        String generatedId = String.valueOf(idCounter++);
        application.setId(generatedId);
        applicationRepository.put(generatedId, application);
        return application;
    }

    public Application updateApplication(String id, Application updatedApplication) {
        if (applicationRepository.containsKey(id)) {
            updatedApplication.setId(id);
            applicationRepository.put(id, updatedApplication);
            return updatedApplication;
        }
        return null;
    }

    public boolean deleteApplication(String id) {
        return applicationRepository.remove(id) != null;
    }
}
