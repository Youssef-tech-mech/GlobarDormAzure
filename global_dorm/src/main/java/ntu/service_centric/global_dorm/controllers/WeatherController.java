package ntu.service_centric.global_dorm.controllers;

import ntu.service_centric.global_dorm.models.api.WeatherResponseDTO;
import ntu.service_centric.global_dorm.services.api.WeatherApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {

    @Autowired
    private WeatherApiService weatherApiService;

    // Serve the weather search page
    @GetMapping("/weather")
    public String showWeatherPage() {
        return "weather"; // Corresponds to weather.html in the templates directory
    }

    // Fetch weather and display it on the result page
    @GetMapping("/api/weather")
    public String getWeatherByCity(@RequestParam String city, Model model) {
        WeatherResponseDTO weatherResponse = weatherApiService.getWeatherByCity(city);
        model.addAttribute("weather", weatherResponse);
        return "weather-result"; // Corresponds to weather-result.html
    }
}
