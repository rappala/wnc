package com.rappala.wnc.entity;

import java.time.LocalDateTime;

public class HourlyDTO {
    private String ldt;
    private Double temp;
    private Double windSpeed;

    public String getLdt() {
        return ldt;
    }

    public void setLdt(String ldt) {
        this.ldt = ldt;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return "HourlyDTO{" +
                "ldt='" + ldt + '\'' +
                ", temp=" + temp +
                ", windSpeed=" + windSpeed +
                '}';
    }
}
