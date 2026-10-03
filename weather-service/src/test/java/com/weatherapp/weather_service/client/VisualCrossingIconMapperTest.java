package com.weatherapp.weather_service.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class VisualCrossingIconMapperTest {

    @ParameterizedTest(name = "{0} → {1}")
    @CsvSource({
            "clear-day,           0",
            "partly-cloudy-night, 2",
            "cloudy,              3",
            "fog,                 45",
            "rain,                63",
            "showers-day,         80",
            "snow,                73",
            "thunder-rain,        95"
    })
    @DisplayName("Visual Crossing ikonlarını WMO kodlarına çevirir")
    void mapsKnownIcons(String icon, int expectedCode) {
        assertThat(VisualCrossingIconMapper.toWeatherCode(icon)).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("Bilinmeyen ya da boş ikon için null döner")
    void unknownIcon() {
        assertThat(VisualCrossingIconMapper.toWeatherCode("uzaylı-istilası")).isNull();
        assertThat(VisualCrossingIconMapper.toWeatherCode(null)).isNull();
    }
}