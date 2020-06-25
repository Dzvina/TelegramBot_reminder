//package com.mdo.teleg_bot;
//
//import org.springframework.stereotype.Service;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class TelegramService {
//
//
//    public ReplyKeyboardMarkup getMainMenu() {
//        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
//
//        KeyboardRow row = new KeyboardRow();
//        row.add(WHAT_THE_TIME);
//        row.add(WHAT_THE_DATE);
//
//        KeyboardRow row1 = new KeyboardRow();
//        row1.add(HOW_LONG);
//        row1.add(HOW_MANY);
//
//        List<KeyboardRow> rows = new ArrayList<>();
//        rows.add(row);
//        rows.add(row1);
//
//        markup.setKeyboard(rows);
//        return markup;
//    }
//
//    public SendMessage clientCity(Message message){
//        SendMessage response = new SendMessage();
//        response.setChatId(message.getChatId());
//
//        response.setText("HI " + message.getFrom().getFirstName()
//                + " " + message.getFrom().getLastName() + ". tell me the city where you are so I can connect to your time zone. ");
//
//        response.setText("Your time is: " + response.setReplyMarkup(getCurrentTimeResponse(message.getM)));
//
//        response.setReplyMarkup(getMainMenu());
//        return response;
//    }
//
//    public SendMessage getCurrentTimeResponse(Message message) {
//        SendMessage response = new SendMessage();
//        String city;
//
//        response.setText(LocalDateTime.now(ZoneId.of(city)).format(DateTimeFormatter.ofPattern("HH:mm")));
//        response.setChatId(message.getChatId());
//
//        response.setReplyMarkup(getMainMenu());
//        return response;
//    }
//
//    public SendMessage getCurrentDateResponse(Message message) {
//        SendMessage response = new SendMessage();
//
//        response.setText(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
//        response.setChatId(message.getChatId());
//
//        response.setReplyMarkup(getMainMenu());
//        return response;
//
//    }
//
//}
