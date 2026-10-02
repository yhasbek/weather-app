package com.weatherapp.weather_service.client;

import com.weatherapp.weather_service.dto.CityDto;
import com.weatherapp.weather_service.exception.CityNotFoundException;
import com.weatherapp.weather_service.exception.ExternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class CityClient {

    private static final Logger log = LoggerFactory.getLogger(CityClient.class);

    private final RestClient restClient;

    public CityClient(@Qualifier("cityRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public CityDto getCity(Long cityId) {
        log.info("city-service'ten şehir isteniyor: cityId={}", cityId);
        try {
            return restClient.get()
                    .uri("/api/cities/{id}", cityId)
                    .retrieve()
                    .body(CityDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("city-service şehri bulamadı: cityId={}", cityId);
            throw new CityNotFoundException(cityId);
        } catch (RestClientException e) {
            log.error("city-service'e ulaşılamadı: cityId={}", cityId, e);
            throw new ExternalServiceException("city-service'e ulaşılamadı", e);
        }
    }
}