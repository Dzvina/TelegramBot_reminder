package com.mdo.teleg_bot.botapi;

public enum BotState {

    GREETING,
    ASK_LOCATION,

    ASK_REMINDER,
    ASK_DATE,
    ASK_TIME,
    ASK_MESSAGE,

    FILLING_PROFILE,
    PROFILE_FILLED,

    MENU_CHANGED,
    SHOW_HELP_MENU,

    SHOW_REMINDER
}
