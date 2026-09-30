package com.weatherapp.weather_service.client;

import com.weatherapp.weather_service.dto.OpenMeteoResponse;
import com.weatherapp.weather_service.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class OpenMeteoClient {

    private static final String DAILY_FIELDS =
            "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max";

    private final RestClient restClient;

    public OpenMeteoClient(@Qualifier("openMeteoRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public OpenMeteoResponse getWeeklyForecast(double latitude, double longitude) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/forecast")
                            .queryParam("latitude", latitude)
                            .queryParam("longitude", longitude)
                            .queryParam("daily", DAILY_FIELDS)
                            .queryParam("timezone", "auto")
                            .queryParam("forecast_days", 7)
                            .build())
                    .retrieve()
                    .body(OpenMeteoResponse.class);
        } catch (RestClientException e) {
            throw new ExternalServiceException("Hava durumu servisine ulaşılamadı", e);
        }
    }
}