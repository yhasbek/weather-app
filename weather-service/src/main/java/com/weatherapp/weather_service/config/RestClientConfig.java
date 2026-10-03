package com.weatherapp.weather_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class RestClientConfig {

    private final int readTimeoutSeconds;

    public RestClientConfig(@Value("${services.http.read-timeout-seconds:5}") int readTimeoutSeconds) {
        this.readTimeoutSeconds = readTimeoutSeconds;
    }

    @Bean
    public RestClient cityRestClient(@Value("${services.city.base-url}") String baseUrl) {
        return buildClient(baseUrl);
    }

    @Bean
    public RestClient openMeteoRestClient(@Value("${open-meteo.base-url}") String baseUrl) {
        return buildClient(baseUrl);
    }

    private RestClient buildClient(String baseUrl) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(readTimeoutSeconds));

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }
}