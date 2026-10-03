package com.weatherapp.weather_service.client;

public final class VisualCrossingIconMapper {

    private VisualCrossingIconMapper() {
    }

    /**
     * Visual Crossing ikon adını, sistemimizin kullandığı WMO hava koduna çevirir.
     */
    public static Integer toWeatherCode(String icon) {
        if (icon == null) {
            return null;
        }
        return switch (icon) {
            case "clear-day", "clear-night" -> 0;
            case "wind" -> 1;
            case "partly-cloudy-day", "partly-cloudy-night" -> 2;
            case "cloudy" -> 3;
            case "fog" -> 45;
            case "rain" -> 63;
            case "sleet" -> 66;
            case "snow" -> 73;
            case "showers-day", "showers-night" -> 80;
            case "snow-showers-day", "snow-showers-night" -> 85;
            case "thunder", "thunder-rain", "thunder-showers-day", "thunder-showers-night" -> 95;
            case "hail" -> 96;
            default -> null;
        };
    }
}