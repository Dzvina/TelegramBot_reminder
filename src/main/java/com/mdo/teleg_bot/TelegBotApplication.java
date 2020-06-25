package com.mdo.teleg_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@SpringBootApplication
public class TelegBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegBotApplication.class, args);


    }
}
