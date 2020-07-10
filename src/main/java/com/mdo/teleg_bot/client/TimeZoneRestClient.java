package com.mdo.teleg_bot.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TimeZoneRestClient {

    private static final String TIME_ZONE_PATH = "https://api.ipgeolocation.io/timezone?apiKey=9f78829c951b441ca68aff20cda6f905&lat=%f&long=%f";

    @Autowired
    private RestTemplate restTemplate;

    public TimezoneOffsetResponse timeZone(double latitude, double longitude) {
        String url = String.format(TIME_ZONE_PATH, latitude, longitude);
        System.out.println(url);
        ResponseEntity<TimezoneOffsetResponse> responseEntity = restTemplate.getForEntity(url, TimezoneOffsetResponse.class);
        TimezoneOffsetResponse timezoneOffsetResponse = responseEntity.getBody();
        System.out.println(timezoneOffsetResponse);
        return timezoneOffsetResponse;
    }


}
