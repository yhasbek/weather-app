package com.weatherapp.weather_service.client;

import com.weatherapp.weather_service.dto.ForecastData;

public interface WeatherProvider {

    ForecastData getWeeklyForecast(double latitude, double longitude);
}