package com.weatherapp.country_service.exception;

import org.springframework.http.HttpStatus;

public class CountryNotFoundException extends RuntimeException {

    public CountryNotFoundException(String code) {
        super("Ülke bulunamadı: " + code);
    }
}