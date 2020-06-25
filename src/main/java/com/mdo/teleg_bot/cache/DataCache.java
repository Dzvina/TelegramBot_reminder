package com.mdo.teleg_bot.cache;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.handlers.fillingprofile.UserProfileData;

public interface DataCache {

    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    UserProfileData getUserProfileData(int userId);

    void saveUserProfileData(int userId, UserProfileData userProfileData);

}
