package com.weatherapp.country_service.dto;

import com.weatherapp.country_service.entity.Country;

public record CountryResponse(String code, String name) {

    public static CountryResponse from(Country country) {
        return new CountryResponse(country.getCode(), country.getName());
    }
}