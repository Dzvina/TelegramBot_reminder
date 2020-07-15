package com.mdo.teleg_bot.botapi.handlers.menu;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.InputMessageHandler;
import com.mdo.teleg_bot.cache.UserDataCache;
import com.mdo.teleg_bot.dao.ReminderDao;
import com.mdo.teleg_bot.model.Reminder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class ShowProfileHandler implements InputMessageHandler {

    private final static String FINAL_MESSAGE = "-------------------%n" +
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
        userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_REMINDER);  //at this moment nothing to do!

        List<Reminder> reminders = reminderDao.getAllRemindersByUserId(message.getFrom().getId());
        String responseString = " ";

        for (int i = 0; i < reminders.size(); i++) {
            responseString += String.format(FINAL_MESSAGE,
                    reminders.get(i).getDate(),
                    reminders.get(i).getTime(),
                    reminders.get(i).getMessage());
        }


        return new SendMessage(message.getChatId(), responseString);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_REMINDER;
    }
}
