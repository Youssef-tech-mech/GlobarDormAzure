package ntu.service_centric.global_dorm.services.api;

import ntu.service_centric.global_dorm.models.api.DistanceResponseDTO;
import ntu.service_centric.global_dorm.services.DistanceServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DistanceApiService {

    @Autowired
    private DistanceServiceClient distanceServiceClient;

    /**
     * Calculate the distance from the selected room to a campus location.
     *
     * @param roomId ID of the room
     * @param campusLat Latitude of the campus
     * @param campusLon Longitude of the campus
     * @return DistanceResponseDTO with the calculated distance
     */
    public DistanceResponseDTO calculateDistanceToCampus(String roomId, double campusLat, double campusLon) {
        // Fetch room details (hardcoded or fetched from DB)
        double roomLat = getRoomLatitude(roomId);
        double roomLon = getRoomLongitude(roomId);

        return distanceServiceClient.calculateDistance(roomLat, roomLon, campusLat, campusLon);
    }

    private double getRoomLatitude(String roomId) {
        // Replace with actual logic to fetch room latitude
        return 52.9548; // Example latitude
    }

    private double getRoomLongitude(String roomId) {
        // Replace with actual logic to fetch room longitude
        return -1.1581; // Example longitude
    }
}