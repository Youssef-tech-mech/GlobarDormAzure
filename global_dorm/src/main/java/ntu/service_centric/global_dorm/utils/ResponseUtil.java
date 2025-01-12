package ntu.service_centric.global_dorm.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ResponseUtil {

    public static String getApiResponse(String url, RestTemplate restTemplate) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch data from API: " + e.getMessage(), e);
        }
    }
}
