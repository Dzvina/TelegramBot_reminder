package com.mdo.teleg_bot.botapi.handlers.menu;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.InputMessageHandler;
import com.mdo.teleg_bot.botapi.handlers.fillingprofile.UserProfileData;
import com.mdo.teleg_bot.cache.UserDataCache;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ShowProfileHandler implements InputMessageHandler {
    private final static String FINAL_MESSAGE = "Data on your reminder%n" +
            "-------------------%n" +
            "Location: %s%n" +
            "Date: %d%n" +
            "Time: %s%n" +
            "Message: %d%n";

    private UserDataCache userDataCache;

    public ShowProfileHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        return new SendMessage(message.getChatId(), String.format(FINAL_MESSAGE, profileData.getLocation(),
                profileData.getDate(), profileData.getTime(), profileData.getMessage()));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }
}
