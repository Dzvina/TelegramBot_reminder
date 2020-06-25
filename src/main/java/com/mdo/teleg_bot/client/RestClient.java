package com.mdo.teleg_bot.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {

    private static final String TIME_ZONE_PATH = "http://vip.timezonedb.com/v2.1/get-time-zone?key=SQ5J1WNIMTIO&format=xml&by=city&city=";

    @Autowired
    private RestTemplate restTemplate;

    public String timeZone(String city) {
        String url = TIME_ZONE_PATH + city;
        System.out.println(url);
        ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity(url, String.class);
        return stringResponseEntity.getBody();
    }
}
