package com.weatherapp.city_service.service;

import com.weatherapp.city_service.dto.CityResponse;
import com.weatherapp.city_service.exception.CityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CityServiceIntegrationTest {

    @Autowired
    private CityService cityService;

    @Test
    @DisplayName("Türkiye'nin şehirlerini Türkçe alfabeye göre sıralı döner")
    void returnsTurkishCitiesSorted() {
        List<CityResponse> cities = cityService.getCitiesByCountry("tr");

        assertThat(cities)
                .extracting(CityResponse::name)
                .containsExactly("Ankara", "Antalya", "Bursa", "İstanbul", "İzmir", "Trabzon");
    }

    @Test
    @DisplayName("Olmayan ülke kodu için boş liste döner")
    void unknownCountryReturnsEmptyList() {
        assertThat(cityService.getCitiesByCountry("XX")).isEmpty();
    }

    @Test
    @DisplayName("Id ile şehri koordinatlarıyla birlikte döner")
    void returnsCityById() {
        CityResponse city = cityService.getCityById(1L);

        assertThat(city.name()).isEqualTo("İstanbul");
        assertThat(city.countryCode()).isEqualTo("TR");
        assertThat(city.latitude()).isEqualTo(41.0082);
        assertThat(city.longitude()).isEqualTo(28.9784);
    }

    @Test
    @DisplayName("Olmayan id için CityNotFoundException fırlatır")
    void unknownIdThrows() {
        assertThatThrownBy(() -> cityService.getCityById(999L))
                .isInstanceOf(CityNotFoundException.class)
                .hasMessageContaining("999");
    }
}