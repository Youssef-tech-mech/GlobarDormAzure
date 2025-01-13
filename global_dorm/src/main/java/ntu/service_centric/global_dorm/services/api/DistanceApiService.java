package ntu.service_centric.global_dorm.services.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class DistanceApiService {

    private static final Logger logger = LoggerFactory.getLogger(DistanceApiService.class);

    @Value("${distance.api.url}")
    private String osrmBaseUrl;

    @Value("${nominatim.api.url}")
    private String nominatimApiUrl;

    private final RestTemplate restTemplate;

    public DistanceApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Resolve city name to coordinates using Nominatim API.
     */
    public String resolveCityToCoordinates(String cityName) {
        String url = String.format("%s/search?q=%s&format=json&limit=1", nominatimApiUrl, cityName);
        logger.info("Resolving coordinates for city: {}", cityName);

        try {
            Map<String, Object>[] response = restTemplate.getForObject(url, Map[].class);

            if (response != null && response.length > 0) {
                String lat = (String) response[0].get("lat");
                String lon = (String) response[0].get("lon");
                logger.info("Resolved coordinates for city {}: {}, {}", cityName, lon, lat);
                return lon + "," + lat;
            } else {
                logger.warn("City not found: {}", cityName);
                throw new RuntimeException("City not found: " + cityName);
            }
        } catch (Exception e) {
            logger.error("Error resolving city name to coordinates: {}", e.getMessage(), e);
            throw new RuntimeException("Error resolving city name to coordinates: " + e.getMessage(), e);
        }
    }

    /**
     * Calculate distance using OSRM API and cache the result.
     *
     * @param profile The routing profile (e.g., driving, cycling, walking).
     * @param startCoordinates Starting coordinates (longitude,latitude).
     * @param endCoordinates Ending coordinates (longitude,latitude).
     * @return The full API response containing routes, waypoints, etc.
     */
    @Cacheable(value = "distances", key = "#profile + '_' + #startCoordinates + '_' + #endCoordinates")
    public Map<String, Object> calculateDistance(String profile, String startCoordinates, String endCoordinates) {
        logger.info("Generated Cache Key: {}_{}_{}", profile, startCoordinates, endCoordinates);
        logger.info("Cache MISS - Calling API for profile: {}, start: {}, end: {}", profile, startCoordinates, endCoordinates);

        String url = String.format("%s/%s/%s;%s", osrmBaseUrl, profile, startCoordinates, endCoordinates);

        try {
            logger.info("Calling OSRM API at URL: {}", url);
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            logger.info("Successfully retrieved distance data for profile: {}, start: {}, end: {}", profile, startCoordinates, endCoordinates);
            return response;
        } catch (Exception e) {
            logger.error("Error calling OSRM Distance API: {}", e.getMessage(), e);
            throw new RuntimeException("Error calling OSRM Distance API: " + e.getMessage(), e);
        }
    }
}
