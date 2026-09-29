package com.weatherapp.city_service.dto;

import com.weatherapp.city_service.entity.City;

public record CityResponse(
        Long id,
        String countryCode,
        String name,
        double latitude,
        double longitude
) {
    public static CityResponse from(City city) {
        return new CityResponse(
                city.getId(),
                city.getCountryCode(),
                city.getName(),
                city.getLatitude(),
                city.getLongitude()
        );
    }
}