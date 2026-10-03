package com.weatherapp.weather_service.service;

import com.weatherapp.weather_service.client.CityClient;
import com.weatherapp.weather_service.client.WeatherProvider;
import com.weatherapp.weather_service.dto.CityDto;
import com.weatherapp.weather_service.dto.DailyForecast;
import com.weatherapp.weather_service.dto.ForecastData;
import com.weatherapp.weather_service.dto.WeatherForecastResponse;
import com.weatherapp.weather_service.exception.CityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private CityClient cityClient;

    @Mock
    private WeatherProvider weatherProvider;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    @DisplayName("Şehir bilgisi ile sağlayıcıdan gelen tahmini birleştirir")
    void combinesCityAndForecast() {
        CityDto istanbul = new CityDto(1L, "TR", "İstanbul", 41.0082, 28.9784);
        List<DailyForecast> days = List.of(
                new DailyForecast(LocalDate.of(2026, 10, 3), 20.4, 15.6, 10, 3, "Kapalı")
        );

        when(cityClient.getCity(1L)).thenReturn(istanbul);
        when(weatherProvider.getWeeklyForecast(41.0082, 28.9784))
                .thenReturn(new ForecastData("Europe/Istanbul", days));

        WeatherForecastResponse result = weatherService.getWeeklyForecast(1L);

        assertThat(result.cityId()).isEqualTo(1L);
        assertThat(result.cityName()).isEqualTo("İstanbul");
        assertThat(result.countryCode()).isEqualTo("TR");
        assertThat(result.timezone()).isEqualTo("Europe/Istanbul");
        assertThat(result.days()).isEqualTo(days);
    }

    @Test
    @DisplayName("Şehir bulunamazsa hatayı iletir ve sağlayıcıya hiç gitmez")
    void cityNotFound() {
        when(cityClient.getCity(999L)).thenThrow(new CityNotFoundException(999L));

        assertThatThrownBy(() -> weatherService.getWeeklyForecast(999L))
                .isInstanceOf(CityNotFoundException.class);

        verifyNoInteractions(weatherProvider);
    }
}