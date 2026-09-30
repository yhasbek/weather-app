package com.weatherapp.weather_service.client;

import com.weatherapp.weather_service.dto.CityDto;
import com.weatherapp.weather_service.exception.CityNotFoundException;
import com.weatherapp.weather_service.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class CityClient {

    private final RestClient restClient;

    public CityClient(@Qualifier("cityRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public CityDto getCity(Long cityId) {
        try {
            return restClient.get()
                    .uri("/api/cities/{id}", cityId)
                    .retrieve()
                    .body(CityDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new CityNotFoundException(cityId);
        } catch (RestClientException e) {
            throw new ExternalServiceException("city-service'e ulaşılamadı", e);
        }
    }
}