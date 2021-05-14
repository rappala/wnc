package com.rappala.wnc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rappala.wnc.entity.Hourly;
import com.rappala.wnc.entity.HourlyDTO;
import com.rappala.wnc.entity.WeatherResponse;
import com.rappala.wnc.entity.WeatherResponseDTO;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WncServiceImpl implements WncService {
    private static final String WEATHER_URI = "https://api.openweathermap.org/data/2.5/onecall?";
    private static final String APP_ID = "7a3cf4f93845a685afa8a37d74d6ab5d";
    private static final String ACCOUNT_SID = "AC95cd2e72907818c57b16f2ad7bf80a82";
    private static final String AUTH_TOKEN = "414108418903c404d6f6532c205381a8";
    private static final String TRAIL_NUMBER = "+13142741942";

    private static Logger log = LoggerFactory.getLogger(WncServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public WeatherResponseDTO getWeatherUsingLatAndLon(String lat, String lon, int temp, int windSpeed) {
        WeatherResponseDTO weatherResponseDTO = getWeatherResponseAndFilter(lat, lon, temp,windSpeed);
        String topThreeList = getTopThreeHourlyMessage(weatherResponseDTO);
        sendSms(topThreeList);
        return weatherResponseDTO;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void scheduleWeatherUpdate(){
        WeatherResponseDTO weatherUsingLatAndLon = getWeatherUsingLatAndLon("37.45461795594225", "-121.91812338570007", 60, 10);
        log.info("current Weather notification : {}", new Date().toString());
    }

    public WeatherResponseDTO getWeatherResponseAndFilter(String lat, String lon, int temp, int windSpeed) {
        ObjectMapper objectMapper = new ObjectMapper();
        String uri = WEATHER_URI + "lat=" + lat + "&lon=" + lon +
                "&exclude=current,minutely,alerts,daily&appid=" + APP_ID + "&units=imperial";
        log.info("constructed uri : {}", uri);
        ResponseEntity<String> weatherResponse = restTemplate.getForEntity(uri, String.class);
        String strBody;
        WeatherResponse response = null;
        if (weatherResponse.getStatusCodeValue() == 200) {
            strBody = weatherResponse.getBody();
            log.info("weather response from server : {}", strBody);
            try {
                response = objectMapper.readValue(strBody, WeatherResponse.class);
                log.info("json response to weather response:{}", response);
                List<Hourly> getHourlyList = response.getHourly();
                ListIterator<Hourly> traverseHourlyList = getHourlyList.listIterator();
                while (traverseHourlyList.hasNext()) {
                    Hourly hourly = traverseHourlyList.next();
                    if (hourly.getTemp() < temp || hourly.getWindSpeed() > windSpeed) {
                        traverseHourlyList.remove();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertWeatherResponseToWeatherResponseDTO(response);
    }
    public HourlyDTO covertHourlyToHourlyDTO(Hourly hourly){
        Long epochTime = hourly.getDt();
        String localDateTime = convertEpochToLocalDateTime(epochTime);
        HourlyDTO hourlyDTO = new HourlyDTO();
        hourlyDTO.setLdt(localDateTime);
        hourlyDTO.setTemp(hourly.getTemp());
        hourlyDTO.setWindSpeed(hourly.getWindSpeed());
        return hourlyDTO;
    }

    public String convertEpochToLocalDateTime(Long dt){
        Date date = new Date(dt*1000L);
        // format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        jdf.setTimeZone(TimeZone.getTimeZone("PST"));
        String javaDate = jdf.format(date);
        return javaDate;
    }

    public WeatherResponseDTO convertWeatherResponseToWeatherResponseDTO(WeatherResponse weatherResponse) {
        List<Hourly> hourlyList = weatherResponse.getHourly();
        List<HourlyDTO> hourlyDTOList = new ArrayList<>();
        ListIterator<Hourly> traverseHourlyList = hourlyList.listIterator();
        while (traverseHourlyList.hasNext()) {
            Hourly hourly = traverseHourlyList.next();
            HourlyDTO hourlyDTO = covertHourlyToHourlyDTO(hourly);
            hourlyDTOList.add(hourlyDTO);
        }
        WeatherResponseDTO weatherResponseDTO = new WeatherResponseDTO();
        weatherResponseDTO.setHourlyDTOList(hourlyDTOList);
        return weatherResponseDTO;
    }

    public String getTopThreeHourlyMessage(WeatherResponseDTO weatherResponseDTO){
        List<HourlyDTO> hourlyDTOList = weatherResponseDTO.getHourlyDTOList();
        String topThreeHourlyNotification = null;
        List<String> stringList = new ArrayList<>();
        for(int i = 0; i <= hourlyDTOList.size(); i++){
            if(i < 3) {
                HourlyDTO hourlyDTO = hourlyDTOList.get(i);
                stringList.add(hourlyDTO.toString());
            }
        }
        topThreeHourlyNotification = String.join(";", stringList);
        log.info("extract top three elements: {}", topThreeHourlyNotification);
        return topThreeHourlyNotification;
    }

    public void sendSms(String topThreeHourlyNotification){
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber("+13159305529"),
                new PhoneNumber(TRAIL_NUMBER),
                topThreeHourlyNotification)
                .create();
        log.info("message sent through twilio: {}", message.getStatus());
    }
}
