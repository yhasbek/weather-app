package com.weatherapp.weather_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherCodeDescriberTest {

    @ParameterizedTest(name = "{0} kodu → {1}")
    @CsvSource({
            "0,  Açık",
            "3,  Kapalı",
            "45, Sisli",
            "48, Sisli",
            "61, Hafif yağmur",
            "80, Sağanak yağış",
            "95, Gök gürültülü fırtına",
            "99, Dolulu fırtına"
    })
    @DisplayName("Bilinen hava kodlarını Türkçe açıklamaya çevirir")
    void describesKnownCodes(int code, String expected) {
        assertThat(WeatherCodeDescriber.describe(code)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Bilinmeyen kod için 'Bilinmiyor' döner")
    void unknownCode() {
        assertThat(WeatherCodeDescriber.describe(1234)).isEqualTo("Bilinmiyor");
    }

    @Test
    @DisplayName("Kod gelmezse (null) çökmez, 'Bilinmiyor' döner")
    void nullCode() {
        assertThat(WeatherCodeDescriber.describe(null)).isEqualTo("Bilinmiyor");
    }
}