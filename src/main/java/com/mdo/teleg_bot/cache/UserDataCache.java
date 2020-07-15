package com.mdo.teleg_bot.cache;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.model.Reminder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
    private Map<Integer, BotState> usersBotStates = new HashMap<>();
    private Map<Long, Reminder> reminderCache = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.ASK_REMINDER;
        }
        return botState;
    }

    @Override
    public Reminder getReminder(long userId) {
        Reminder reminder = reminderCache.get(userId);
        if (reminder == null) {
            reminder = new Reminder();
        }
        return reminder;
    }

    @Override
    public void saveReminder(long userId, Reminder reminder) {
        reminderCache.put(userId, reminder);
    }

    @Override
    public void deleteReminderFromCache(long userId) {
        reminderCache.remove(userId);
    }
}
