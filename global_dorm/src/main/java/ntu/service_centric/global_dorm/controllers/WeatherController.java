package ntu.service_centric.global_dorm.controllers;

import ntu.service_centric.global_dorm.models.api.WeatherResponseDTO;
import ntu.service_centric.global_dorm.services.api.WeatherApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherApiService weatherApiService;

    @GetMapping
    public ResponseEntity<WeatherResponseDTO> getWeatherByCity(@RequestParam String city) {
        WeatherResponseDTO weatherResponse = weatherApiService.getWeatherByCity(city);
        return ResponseEntity.ok(weatherResponse);
    }
}
