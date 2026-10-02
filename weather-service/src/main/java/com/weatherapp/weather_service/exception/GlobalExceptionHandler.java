package com.weatherapp.weather_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CityNotFoundException.class)
    public ProblemDetail handleCityNotFound(CityNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Şehir bulunamadı");
        return problem;
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ProblemDetail handleExternalService(ExternalServiceException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Hava durumu bilgisi şu an alınamıyor. Lütfen biraz sonra tekrar deneyin.");
        problem.setTitle("Servis geçici olarak kullanılamıyor");
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Beklenmeyen bir hata oluştu", ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Beklenmeyen bir hata oluştu. Lütfen daha sonra tekrar deneyin.");
        problem.setTitle("Sunucu hatası");
        return problem;
    }
}