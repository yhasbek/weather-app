package com.weatherapp.city_service.repository;

import com.weatherapp.city_service.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findByCountryCodeIgnoreCase(String countryCode);
}