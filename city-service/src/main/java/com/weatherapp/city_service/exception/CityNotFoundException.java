package com.weatherapp.city_service.exception;

import org.springframework.http.HttpStatus;

public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(Long id) {
        super("Şehir bulunamadı: " + id);
    }
}