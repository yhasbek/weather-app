package com.weatherapp.weather_service.service;

import com.weatherapp.weather_service.client.CityClient;
import com.weatherapp.weather_service.client.OpenMeteoClient;
import com.weatherapp.weather_service.dto.CityDto;
import com.weatherapp.weather_service.dto.DailyForecast;
import com.weatherapp.weather_service.dto.OpenMeteoResponse;
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
    private OpenMeteoClient openMeteoClient;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    @DisplayName("Şehir bilgisi ile tahmini birleştirip günlük listeye çevirir")
    void combinesCityAndForecast() {
        // Given: sahte nesnelerin ne döneceğini ayarla
        CityDto istanbul = new CityDto(1L, "TR", "İstanbul", 41.0082, 28.9784);
        OpenMeteoResponse.Daily daily = new OpenMeteoResponse.Daily(
                List.of("2026-10-01", "2026-10-02"),
                List.of(3, 80),
                List.of(20.4, 19.1),
                List.of(15.6, 14.8),
                List.of(10, 65)
        );
        OpenMeteoResponse forecast = new OpenMeteoResponse("Europe/Istanbul", daily);

        when(cityClient.getCity(1L)).thenReturn(istanbul);
        when(openMeteoClient.getWeeklyForecast(41.0082, 28.9784)).thenReturn(forecast);

        // When: test ettiğimiz metodu çağır
        WeatherForecastResponse result = weatherService.getWeeklyForecast(1L);

        // Then: sonucu doğrula
        assertThat(result.cityId()).isEqualTo(1L);
        assertThat(result.cityName()).isEqualTo("İstanbul");
        assertThat(result.timezone()).isEqualTo("Europe/Istanbul");
        assertThat(result.days()).hasSize(2);

        DailyForecast secondDay = result.days().get(1);
        assertThat(secondDay.date()).isEqualTo(LocalDate.of(2026, 10, 2));
        assertThat(secondDay.maxTemperature()).isEqualTo(19.1);
        assertThat(secondDay.minTemperature()).isEqualTo(14.8);
        assertThat(secondDay.precipitationProbability()).isEqualTo(65);
        assertThat(secondDay.weatherCode()).isEqualTo(80);
        assertThat(secondDay.description()).isEqualTo("Sağanak yağış");
    }

    @Test
    @DisplayName("Şehir bulunamazsa hatayı iletir ve Open-Meteo'ya hiç gitmez")
    void cityNotFound() {
        when(cityClient.getCity(999L)).thenThrow(new CityNotFoundException(999L));

        assertThatThrownBy(() -> weatherService.getWeeklyForecast(999L))
                .isInstanceOf(CityNotFoundException.class);

        verifyNoInteractions(openMeteoClient);
    }
}