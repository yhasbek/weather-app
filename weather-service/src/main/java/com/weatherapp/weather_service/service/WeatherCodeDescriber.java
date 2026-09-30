package com.weatherapp.weather_service.service;

public final class WeatherCodeDescriber {

    private WeatherCodeDescriber() {
    }

    public static String describe(Integer code) {
        if (code == null) {
            return "Bilinmiyor";
        }
        return switch (code) {
            case 0 -> "Açık";
            case 1 -> "Çoğunlukla açık";
            case 2 -> "Parçalı bulutlu";
            case 3 -> "Kapalı";
            case 45, 48 -> "Sisli";
            case 51, 53, 55 -> "Çisenti";
            case 56, 57 -> "Donan çisenti";
            case 61 -> "Hafif yağmur";
            case 63 -> "Yağmur";
            case 65 -> "Şiddetli yağmur";
            case 66, 67 -> "Donan yağmur";
            case 71 -> "Hafif kar";
            case 73 -> "Kar";
            case 75 -> "Yoğun kar";
            case 77 -> "Kar taneleri";
            case 80, 81, 82 -> "Sağanak yağış";
            case 85, 86 -> "Kar sağanağı";
            case 95 -> "Gök gürültülü fırtına";
            case 96, 99 -> "Dolulu fırtına";
            default -> "Bilinmiyor";
        };
    }
}