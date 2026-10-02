package com.weatherapp.weather_service.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}