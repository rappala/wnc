package com.rappala.wnc.service;

import com.rappala.wnc.entity.WeatherResponseDTO;

public interface WncService {
    public WeatherResponseDTO getWeatherUsingLatAndLon(String lat, String lon, int temp, int windSpeed);
}
