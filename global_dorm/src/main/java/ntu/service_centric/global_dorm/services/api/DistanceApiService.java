package ntu.service_centric.global_dorm.services.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class DistanceApiService {

    @Value("${distance.api.url}")
    private String osrmBaseUrl;

    @Value("${nominatim.api.url}")
    private String nominatimApiUrl;

    private final RestTemplate restTemplate;

    public DistanceApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Resolve city name to coordinates using Nominatim API
    public String resolveCityToCoordinates(String cityName) {
        String url = String.format("%s/search?q=%s&format=json&limit=1", nominatimApiUrl, cityName);

        try {
            Map<String, Object>[] response = restTemplate.getForObject(url, Map[].class);

            if (response != null && response.length > 0) {
                String lat = (String) response[0].get("lat");
                String lon = (String) response[0].get("lon");
                return lon + "," + lat;
            } else {
                throw new RuntimeException("City not found: " + cityName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error resolving city name to coordinates: " + e.getMessage(), e);
        }
    }

    // Calculate distance using OSRM API
    public Map<String, Object> calculateDistance(String profile, String startCoordinates, String endCoordinates) {
        String url = String.format("%s/%s/%s;%s", osrmBaseUrl, profile, startCoordinates, endCoordinates);

        try {
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error calling OSRM Distance API: " + e.getMessage(), e);
        }
    }
}