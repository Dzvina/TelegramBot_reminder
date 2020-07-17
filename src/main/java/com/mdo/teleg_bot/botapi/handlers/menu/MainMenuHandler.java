package com.mdo.teleg_bot.botapi.handlers.menu;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.InputMessageHandler;
import com.mdo.teleg_bot.cache.UserDataCache;
import com.mdo.teleg_bot.service.MainMenuService;
import com.mdo.teleg_bot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.mdo.teleg_bot.botapi.handlers.inlinekeybordmenu.InlineKeyboardMenuHandler.getInlineMessageButtons;
import static com.mdo.teleg_bot.staticdata.Messages.*;

@Component
public class MainMenuHandler implements InputMessageHandler {

    private static final ReplyKeyboardMarkup MAIN_MENU = MainMenuService.getMainMenuKeyboard();
    private static final ReplyKeyboardMarkup LOCATION_MENU = MainMenuService.getLocationMenuKeyboard();
    private static final ReplyKeyboardMarkup REMINDER_MENU = MainMenuService.getReminderMenuKeyboard();

    private ReplyMessageService messageService;
    private UserDataCache userDataCache;


    public MainMenuHandler(ReplyMessageService messageService,
                           UserDataCache userDataCache) {
        this.messageService = messageService;
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage replyToUser;

        switch (message.getText()) {
            case REMINDER:
                replyToUser = messageService.getReplyMessage(message.getChatId(), "reply.reminderMenu");
                replyToUser.setReplyMarkup(REMINDER_MENU);
                break;
            case SETTINGS:
                replyToUser = messageService.getReplyMessage(message.getChatId(), "reply.settingMenu");
                replyToUser.setReplyMarkup(LOCATION_MENU);
                break;
            case BACK:
                replyToUser = messageService.getReplyMessage(message.getChatId(), "reply.mainMenu");
                replyToUser.setReplyMarkup(MAIN_MENU);
                break;
            default:
                replyToUser = messageService.getReplyMessage(message.getChatId(), "reply.askReminder");
                userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_REMINDER);
                replyToUser.setReplyMarkup(getInlineMessageButtons());
                break;
        }
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MENU_CHANGED;
    }
}
