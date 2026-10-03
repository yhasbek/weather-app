package com.weatherapp.weather_service.dto;

import java.util.List;

public record ForecastData(String timezone, List<DailyForecast> days) {
}