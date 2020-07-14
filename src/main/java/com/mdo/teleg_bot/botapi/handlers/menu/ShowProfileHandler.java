package com.mdo.teleg_bot.botapi.handlers.menu;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.InputMessageHandler;
import com.mdo.teleg_bot.cache.UserDataCache;
import com.mdo.teleg_bot.dao.ReminderDao;
import com.mdo.teleg_bot.model.Reminder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ShowProfileHandler implements InputMessageHandler {

    private final static String FINAL_MESSAGE = "Data on your reminder%n" +
            "-------------------%n" +
            "Date: %s%n" +
            "Time: %s%n" +
            "Message: %s%n";

    private UserDataCache userDataCache;
    private final ReminderDao reminderDao;

    public ShowProfileHandler(UserDataCache userDataCache, ReminderDao reminderDao) {
        this.userDataCache = userDataCache;
        this.reminderDao = reminderDao;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final Reminder reminder = userDataCache.getReminder(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.MENU_CHANGED);
        //reminderDao.addNewReminder(reminder);

        return new SendMessage(message.getChatId(), String.format(FINAL_MESSAGE,
                reminder.getDate(), reminder.getTime(), reminder.getMessage()));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_REMINDER;
    }
}
