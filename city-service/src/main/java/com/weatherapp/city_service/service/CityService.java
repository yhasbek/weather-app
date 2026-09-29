package com.weatherapp.city_service.service;

import com.weatherapp.city_service.dto.CityResponse;
import com.weatherapp.city_service.exception.CityNotFoundException;
import com.weatherapp.city_service.repository.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class CityService {

    private static final Collator TURKISH = Collator.getInstance(Locale.forLanguageTag("tr"));

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<CityResponse> getCitiesByCountry(String countryCode) {
        return cityRepository.findByCountryCodeIgnoreCase(countryCode).stream()
                .map(CityResponse::from)
                .sorted(Comparator.comparing(CityResponse::name, TURKISH))
                .toList();
    }

    public CityResponse getCityById(Long id) {
        return cityRepository.findById(id)
                .map(CityResponse::from)
                .orElseThrow(() -> new CityNotFoundException(id));
    }
}