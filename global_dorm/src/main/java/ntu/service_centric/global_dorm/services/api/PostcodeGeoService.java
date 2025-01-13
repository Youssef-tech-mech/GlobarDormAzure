package ntu.service_centric.global_dorm.services.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PostcodeGeoService {

    private static final Logger logger = LoggerFactory.getLogger(PostcodeGeoService.class);
    private final RestTemplate restTemplate;

    @Autowired
    public PostcodeGeoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Resolve coordinates for a given UK postcode using Postcodes.io and cache the result.
     *
     * @param postcode the UK postcode
     * @return a string representing the coordinates in "longitude,latitude" format
     */
    @Cacheable(value = "postcodes", key = "#postcode.trim().toUpperCase()")
    public String getCoordinates(String postcode) {
        logger.info("Generated Cache Key: {}", postcode.trim().toUpperCase());
        logger.info("Cache MISS - Fetching coordinates for postcode: {}", postcode);

        String url = "https://api.postcodes.io/postcodes/" + postcode.trim().toUpperCase();
        try {
            logger.info("Calling Postcodes.io API at URL: {}", url);
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && "200".equals(responseBody.get("status").toString())) {
                Map<String, Object> result = (Map<String, Object>) responseBody.get("result");
                String coordinates = result.get("longitude") + "," + result.get("latitude");
                logger.info("Retrieved coordinates: {} for postcode: {}", coordinates, postcode);
                return coordinates;
            } else {
                logger.warn("Invalid or unrecognized postcode: {}", postcode);
                throw new IllegalArgumentException("Invalid or unrecognized postcode: " + postcode);
            }
        } catch (Exception e) {
            logger.error("Error retrieving postcode data: {}", e.getMessage(), e);
            throw new RuntimeException("Error retrieving postcode data: " + e.getMessage(), e);
        }
    }
}
