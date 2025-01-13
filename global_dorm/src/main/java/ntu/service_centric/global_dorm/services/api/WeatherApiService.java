package ntu.service_centric.global_dorm.services.api;

import com.google.gson.Gson;
import ntu.service_centric.global_dorm.models.api.WeatherResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherApiService {

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.key}")
    private String weatherApiKey;

    private final RestTemplate restTemplate;
    private final Gson gson;

    // Cache with expiration
    private final ConcurrentHashMap<String, CachedWeather> weatherCache = new ConcurrentHashMap<>();

    private static final long CACHE_EXPIRY_SECONDS = 3600; // 1 hour

    public WeatherApiService(RestTemplate restTemplate, Gson gson) {
        this.restTemplate = restTemplate;
        this.gson = gson;
    }

    public WeatherResponseDTO getWeatherByCity(String city) {
        // Check the cache first
        CachedWeather cachedWeather = weatherCache.get(city.toLowerCase());
        if (cachedWeather != null && !cachedWeather.isExpired()) {
            return cachedWeather.getWeatherResponse();
        }

        // Construct the API URL
        String url = String.format("%s?q=%s&appid=%s&units=metric", weatherApiUrl, city, weatherApiKey);

        try {
            // Fetch raw JSON response
            String jsonResponse = restTemplate.getForObject(url, String.class);

            // Deserialize JSON into WeatherResponseDTO using Gson
            WeatherResponseDTO weatherResponse = gson.fromJson(jsonResponse, WeatherResponseDTO.class);

            // Cache the response
            weatherCache.put(city.toLowerCase(), new CachedWeather(weatherResponse));

            return weatherResponse;

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to fetch weather data. Error: " + e.getStatusCode(), e);
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Network error while accessing the weather service.", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while fetching weather data.", e);
        }
    }

    // Inner class to handle cached weather data
    private static class CachedWeather {
        private final WeatherResponseDTO weatherResponse;
        private final LocalDateTime timestamp;

        public CachedWeather(WeatherResponseDTO weatherResponse) {
            this.weatherResponse = weatherResponse;
            this.timestamp = LocalDateTime.now();
        }

        public WeatherResponseDTO getWeatherResponse() {
            return weatherResponse;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(timestamp.plusSeconds(CACHE_EXPIRY_SECONDS));
        }
    }
}
