package com.weatherapp.weather_service.client;

import com.weatherapp.weather_service.dto.DailyForecast;
import com.weatherapp.weather_service.dto.ForecastData;
import com.weatherapp.weather_service.dto.VisualCrossingResponse;
import com.weatherapp.weather_service.exception.ExternalServiceException;
import com.weatherapp.weather_service.service.WeatherCodeDescriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class VisualCrossingClient implements WeatherProvider {

    private static final Logger log = LoggerFactory.getLogger(VisualCrossingClient.class);
    private static final int FORECAST_DAYS = 7;
    private static final String TIMELINE_PATH =
            "/VisualCrossingWebServices/rest/services/timeline/{location}/{start}/{end}";

    private final RestClient restClient;
    private final String apiKey;

    public VisualCrossingClient(@Qualifier("visualCrossingRestClient") RestClient restClient,
                                @Value("${visual-crossing.api-key}") String apiKey) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    @Override
    public ForecastData getWeeklyForecast(double latitude, double longitude) {
        LocalDate start = LocalDate.now(ZoneOffset.UTC);
        LocalDate end = start.plusDays(FORECAST_DAYS - 1);
        String location = latitude + "," + longitude;

        log.info("Visual Crossing'den tahmin isteniyor: lat={}, lon={}", latitude, longitude);
        try {
            VisualCrossingResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(TIMELINE_PATH)
                            .queryParam("unitGroup", "metric")
                            .queryParam("include", "days")
                            .queryParam("iconSet", "icons2")
                            .queryParam("contentType", "json")
                            .queryParam("key", apiKey)
                            .build(location, start, end))
                    .retrieve()
                    .body(VisualCrossingResponse.class);
            return toForecastData(response);
        } catch (RestClientException e) {
            log.error("Hava durumu sağlayıcısına ulaşılamadı: lat={}, lon={}", latitude, longitude, e);
            throw new ExternalServiceException("Visual Crossing'e ulaşılamadı", e);
        }
    }

    private ForecastData toForecastData(VisualCrossingResponse response) {
        List<DailyForecast> days = response.days().stream()
                .limit(FORECAST_DAYS)
                .map(this::toDailyForecast)
                .toList();
        return new ForecastData(response.timezone(), days);
    }

    private DailyForecast toDailyForecast(VisualCrossingResponse.Day day) {
        Integer code = VisualCrossingIconMapper.toWeatherCode(day.icon());
        Integer precipitation = day.precipprob() == null ? null : (int) Math.round(day.precipprob());
        return new DailyForecast(
                LocalDate.parse(day.datetime()),
                day.tempmax(),
                day.tempmin(),
                precipitation,
                code,
                WeatherCodeDescriber.describe(code)
        );
    }
}