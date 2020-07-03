package com.mdo.teleg_bot.botapi.handlers.fillingprofile;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.InputMessageHandler;
import com.mdo.teleg_bot.botapi.handlers.calendar.Calendar;
import com.mdo.teleg_bot.cache.UserDataCache;
import com.mdo.teleg_bot.service.MainMenuService;
import com.mdo.teleg_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService messageService;
    private MainMenuService mainMenuService;
    private Calendar calendar;

    public FillingProfileHandler(UserDataCache userDataCache,
                                 ReplyMessageService messageService,
                                 MainMenuService mainMenuService, Calendar calendar) {
        this.userDataCache = userDataCache;
        this.messageService = messageService;
        this.mainMenuService = mainMenuService;
        this.calendar = calendar;
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
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_DATE);
        }

        if (botState.equals(BotState.ASK_DATE)) {
            profileData.setLocation(userAnswer);
            replyToUser = messageService.getReplyMessage(chatId, "reply.askDate");
            replyToUser.setReplyMarkup(calendar.getCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue()));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TIME);
        }

        if (botState.equals(BotState.ASK_TIME)) {
            replyToUser = messageService.getReplyMessage(chatId, "reply.askTime");
            profileData.setDate(LocalDate.parse(userAnswer, DateTimeFormatter.ofPattern("d/MM/yyyy")));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_MESSAGE);
        }

        if (botState.equals(BotState.ASK_MESSAGE)) {
            replyToUser = messageService.getReplyMessage(chatId, "reply.askMessage");
            profileData.setTime(LocalTime.parse(userAnswer, DateTimeFormatter.ofPattern("HH:mm")));
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setMessage(userAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
            //replyToUser = messageService.getReplyMessage(chatId, "reply.profileFilled");
            replyToUser = mainMenuService.getMainMenuMessage(chatId, "Profile filled");
        }

        userDataCache.saveUserProfileData(userId, profileData);
        return replyToUser;
    }



}
