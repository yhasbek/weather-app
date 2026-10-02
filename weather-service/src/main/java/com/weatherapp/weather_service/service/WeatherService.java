package com.weatherapp.weather_service.service;

import com.weatherapp.weather_service.client.CityClient;
import com.weatherapp.weather_service.client.OpenMeteoClient;
import com.weatherapp.weather_service.dto.CityDto;
import com.weatherapp.weather_service.dto.DailyForecast;
import com.weatherapp.weather_service.dto.OpenMeteoResponse;
import com.weatherapp.weather_service.dto.WeatherForecastResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class WeatherService {

    private final CityClient cityClient;
    private final OpenMeteoClient openMeteoClient;
    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    public WeatherService(CityClient cityClient, OpenMeteoClient openMeteoClient) {
        this.cityClient = cityClient;
        this.openMeteoClient = openMeteoClient;
    }

    @Cacheable("forecasts")
    public WeatherForecastResponse getWeeklyForecast(Long cityId) {
        log.info("Haftalık tahmin hazırlanıyor: cityId={}", cityId);
        CityDto city = cityClient.getCity(cityId);
        OpenMeteoResponse forecast = openMeteoClient.getWeeklyForecast(city.latitude(), city.longitude());

        List<DailyForecast> days = toDailyForecasts(forecast.daily());

        return new WeatherForecastResponse(
                city.id(),
                city.name(),
                city.countryCode(),
                forecast.timezone(),
                days
        );
    }

    private List<DailyForecast> toDailyForecasts(OpenMeteoResponse.Daily daily) {
        return IntStream.range(0, daily.time().size())
                .mapToObj(i -> {
                    Integer code = daily.weatherCode().get(i);
                    return new DailyForecast(
                            LocalDate.parse(daily.time().get(i)),
                            daily.temperatureMax().get(i),
                            daily.temperatureMin().get(i),
                            daily.precipitationProbability().get(i),
                            code,
                            WeatherCodeDescriber.describe(code)
                    );
                })
                .toList();
    }
}