package ntu.service_centric.global_dorm.services;

import ntu.service_centric.global_dorm.models.api.DistanceResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class DistanceServiceClient {

    private static final String DISTANCE_API_URL = "https://api.openrouteservice.org/v2/directions/driving-car";

    /**
     * Call the external API to calculate the distance between two coordinates.
     *
     * @param startLat Latitude of the starting location
     * @param startLon Longitude of the starting location
     * @param endLat Latitude of the destination
     * @param endLon Longitude of the destination
     * @return DistanceResponseDTO with calculated distance
     */
    public DistanceResponseDTO calculateDistance(double startLat, double startLon, double endLat, double endLon) {
        RestTemplate restTemplate = new RestTemplate();

        String requestUrl = DISTANCE_API_URL + "?start=" + startLon + "," + startLat + "&end=" + endLon + "," + endLat;

        // Replace with your API key
        String apiKey = "YOUR_API_KEY";
        requestUrl += "&api_key=" + apiKey;

        // Call the API
        double distance = restTemplate.getForObject(requestUrl, Double.class);

        return new DistanceResponseDTO(null, distance, "meters");
    }
}