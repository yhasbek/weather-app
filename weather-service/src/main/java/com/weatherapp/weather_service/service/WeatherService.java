package com.weatherapp.weather_service.service;

import com.weatherapp.weather_service.client.CityClient;
import com.weatherapp.weather_service.client.WeatherProvider;
import com.weatherapp.weather_service.dto.CityDto;
import com.weatherapp.weather_service.dto.ForecastData;
import com.weatherapp.weather_service.dto.WeatherForecastResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final CityClient cityClient;
    private final WeatherProvider weatherProvider;

    public WeatherService(CityClient cityClient, WeatherProvider weatherProvider) {
        this.cityClient = cityClient;
        this.weatherProvider = weatherProvider;
    }

    @Cacheable("forecasts")
    public WeatherForecastResponse getWeeklyForecast(Long cityId) {
        log.info("Haftalık tahmin hazırlanıyor: cityId={}", cityId);
        CityDto city = cityClient.getCity(cityId);
        ForecastData forecast = weatherProvider.getWeeklyForecast(city.latitude(), city.longitude());

        return new WeatherForecastResponse(
                city.id(),
                city.name(),
                city.countryCode(),
                forecast.timezone(),
                forecast.days()
        );
    }
}