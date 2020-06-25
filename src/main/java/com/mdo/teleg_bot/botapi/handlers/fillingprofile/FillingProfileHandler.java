package com.mdo.teleg_bot.botapi.handlers.fillingprofile;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.InputMessageHandler;
import com.mdo.teleg_bot.cache.UserDataCache;
import com.mdo.teleg_bot.client.RestClient;
import com.mdo.teleg_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService messageService;

    public FillingProfileHandler(UserDataCache userDataCache,
                                 ReplyMessageService messageService) {
        this.userDataCache = userDataCache;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_LOCATION);
        }
        return prosessUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage prosessUserInput(Message inputMsg) {

        String userAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_LOCATION)) {
            replyToUser = messageService.getReplyMessage(chatId, "reply.askLocation");
            userDataCache.setUsersCurrentBotState(userId, BotState.GET_TIMEZONE);
        }

        if (botState.equals(BotState.GET_TIMEZONE)) {
            profileData.setLocation(userAnswer);
            replyToUser = messageService.getReplyMessage(chatId, "reply.askDate");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_DATE);
        }

        if (botState.equals(BotState.ASK_DATE)) {
            replyToUser = messageService.getReplyMessage(chatId, "reply.askTime");
            profileData.setDate(LocalDate.parse(userAnswer, DateTimeFormatter.ofPattern("d/MM/yyyy")));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TIME);
        }

        if (botState.equals(BotState.ASK_TIME)) {
            replyToUser = messageService.getReplyMessage(chatId, "reply.askMessage");
            profileData.setTime(LocalTime.parse(userAnswer, DateTimeFormatter.ofPattern("HH:mm")));
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setMessage(userAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
            replyToUser = messageService.getReplyMessage(chatId, "reply.profileFilled");
//            replyToUser= new SendMessage(chatId, String.format("%s %s", "Data on your reminder", profileData));
        }

        userDataCache.saveUserProfileData(userId, profileData);
        return replyToUser;
    }
}
