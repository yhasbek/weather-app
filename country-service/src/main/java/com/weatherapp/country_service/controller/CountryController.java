package com.weatherapp.country_service.controller;

import com.weatherapp.country_service.dto.CountryResponse;
import com.weatherapp.country_service.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<CountryResponse> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/{code}")
    public CountryResponse getCountryByCode(@PathVariable String code) {
        return countryService.getCountryByCode(code);
    }
}