package com.mdo.teleg_bot.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

@Component
public class TelegramRestClient {

    private static final String TELEGRAM_PATH = "https://api.telegram.org/bot";

    @Value("${telegrambot.botToken}")
    private String botToken;


    @Autowired
    private RestTemplate restTemplate;

    public void deleteTelegramMessage(DeleteMessage deleteMessage){
        String url = TELEGRAM_PATH + botToken + "/" + deleteMessage.PATH;
        restTemplate.postForLocation(url, deleteMessage);
    }

    public void sendTelegramMessage(SendMessage sendMessage){
        String url = TELEGRAM_PATH + botToken + "/" + sendMessage.PATH;
        restTemplate.postForLocation(url, sendMessage);
    }

}
