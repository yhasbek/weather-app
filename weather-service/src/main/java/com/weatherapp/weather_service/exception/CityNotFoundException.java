package com.weatherapp.weather_service.exception;

import org.springframework.http.HttpStatus;


public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(Long cityId) {
        super("Şehir bulunamadı: " + cityId);
    }
}