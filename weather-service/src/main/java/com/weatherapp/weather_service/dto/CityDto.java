package com.weatherapp.weather_service.dto;

public record CityDto(
        Long id,
        String countryCode,
        String name,
        double latitude,
        double longitude
) {
}