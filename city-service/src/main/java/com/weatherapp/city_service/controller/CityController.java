package com.weatherapp.city_service.controller;

import com.weatherapp.city_service.dto.CityResponse;
import com.weatherapp.city_service.service.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<CityResponse> getCitiesByCountry(@RequestParam String countryCode) {
        return cityService.getCitiesByCountry(countryCode);
    }

    @GetMapping("/{id}")
    public CityResponse getCityById(@PathVariable Long id) {
        return cityService.getCityById(id);
    }
}