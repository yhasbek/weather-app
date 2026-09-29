package com.weatherapp.country_service.repository;

import com.weatherapp.country_service.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByCodeIgnoreCase(String code);
}