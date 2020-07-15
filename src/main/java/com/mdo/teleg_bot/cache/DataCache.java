package com.mdo.teleg_bot.cache;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.model.Reminder;

public interface DataCache {

    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    Reminder getReminder(long userId);

    void saveReminder(long userId, Reminder reminder);

    void deleteReminderFromCache(long userId);
}
