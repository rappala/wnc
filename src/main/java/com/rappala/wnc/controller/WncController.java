package com.rappala.wnc.controller;

import com.rappala.wnc.entity.HourlyDTO;
import com.rappala.wnc.entity.WeatherResponseDTO;
import com.rappala.wnc.service.WncService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(path="/wnc")
public class WncController {
    private static Logger log = LoggerFactory.getLogger(WncController.class);
    @Autowired WncService wncService;



    @GetMapping(path="/getweather")
    public @ResponseBody WeatherResponseDTO getWeather(@RequestParam String lat, @RequestParam String lon, @RequestParam int temp, @RequestParam int windSpeed){
        log.info("lat {}, lon {}, temp {}, windSpeed {}" , lat,lon,temp,windSpeed);
        WeatherResponseDTO weatherResponseDTO =  wncService.getWeatherUsingLatAndLon(lat, lon, temp, windSpeed);
        return weatherResponseDTO;
    }
}
