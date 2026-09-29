package com.weatherapp.country_service.service;

import com.weatherapp.country_service.dto.CountryResponse;
import com.weatherapp.country_service.exception.CountryNotFoundException;
import com.weatherapp.country_service.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class CountryService {

    private static final Collator TURKISH = Collator.getInstance(Locale.forLanguageTag("tr"));

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryResponse> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(CountryResponse::from)
                .sorted(Comparator.comparing(CountryResponse::name, TURKISH))
                .toList();
    }

    public CountryResponse getCountryByCode(String code) {
        return countryRepository.findByCodeIgnoreCase(code)
                .map(CountryResponse::from)
                .orElseThrow(() -> new CountryNotFoundException(code));
    }
}