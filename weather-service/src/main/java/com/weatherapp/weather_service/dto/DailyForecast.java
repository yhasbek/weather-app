package com.weatherapp.weather_service.dto;

import java.time.LocalDate;

public record DailyForecast(
        LocalDate date,
        Double maxTemperature,
        Double minTemperature,
        Integer precipitationProbability,
        Integer weatherCode,
        String description
) {
}