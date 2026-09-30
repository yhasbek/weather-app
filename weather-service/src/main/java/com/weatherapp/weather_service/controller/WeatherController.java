package com.weatherapp.weather_service.controller;

import com.weatherapp.weather_service.dto.WeatherForecastResponse;
import com.weatherapp.weather_service.service.WeatherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{cityId}")
    public WeatherForecastResponse getWeeklyForecast(@PathVariable Long cityId) {
        return weatherService.getWeeklyForecast(cityId);
    }
}