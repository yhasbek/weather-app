package com.weatherapp.weather_service.dto;

import java.util.List;

public record WeatherForecastResponse(
        Long cityId,
        String cityName,
        String countryCode,
        String timezone,
        List<DailyForecast> days
) {
}