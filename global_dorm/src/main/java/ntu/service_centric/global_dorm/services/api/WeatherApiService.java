package ntu.service_centric.global_dorm.services.api;

import com.google.gson.Gson;
import ntu.service_centric.global_dorm.models.api.WeatherResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
public class WeatherApiService {

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.key}")
    private String weatherApiKey;

    private final RestTemplate restTemplate;
    private final Gson gson;

    public WeatherApiService(RestTemplate restTemplate, Gson gson) {
        this.restTemplate = restTemplate;
        this.gson = gson;
    }

    public WeatherResponseDTO getWeatherByCity(String city) {
        // Construct the API URL
        String url = String.format("%s?q=%s&appid=%s&units=metric", weatherApiUrl, city, weatherApiKey);

        try {
            // Fetch raw JSON response
            String jsonResponse = restTemplate.getForObject(url, String.class);

            // Deserialize JSON into WeatherResponseDTO using Gson
            return gson.fromJson(jsonResponse, WeatherResponseDTO.class);

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to fetch weather data. Error: " + e.getStatusCode(), e);
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Network error while accessing the weather service.", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while fetching weather data.", e);
        }
    }
}
