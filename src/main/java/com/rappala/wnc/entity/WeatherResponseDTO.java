package com.rappala.wnc.entity;

import java.util.ArrayList;
import java.util.List;

public class WeatherResponseDTO {
    private List<HourlyDTO> hourlyDTOList = new ArrayList<>();

    public List<HourlyDTO> getHourlyDTOList() {
        return hourlyDTOList;
    }

    public void setHourlyDTOList(List<HourlyDTO> hourlyDTOList) {
        this.hourlyDTOList = hourlyDTOList;
    }
}
