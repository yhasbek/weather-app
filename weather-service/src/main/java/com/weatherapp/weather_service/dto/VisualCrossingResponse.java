package com.weatherapp.weather_service.dto;

import java.util.List;

public record VisualCrossingResponse(String timezone, List<Day> days) {

    public record Day(
            String datetime,
            Double tempmax,
            Double tempmin,
            Double precipprob,
            String icon
    ) {
    }
}