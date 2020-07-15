package com.mdo.teleg_bot.botapi;

import com.mdo.teleg_bot.botapi.handlers.callbackquery.CallbackQueryHandler;
import com.mdo.teleg_bot.cache.UserDataCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.mdo.teleg_bot.staticdata.Messages.*;

@Component
@Slf4j
public class TelegramFacade {

    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private CallbackQueryHandler callbackQueryHandler;

    public TelegramFacade(BotStateContext botStateContext,
                          UserDataCache userDataCache,
                          CallbackQueryHandler callbackQueryHandler) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.callbackQueryHandler = callbackQueryHandler;
    }


    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        }


        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }


    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case START:
                botState = BotState.GREETING;
                break;
            case REMINDER:
                botState = BotState.MENU_CHANGED;
                break;
            case MY_REMINDERS:
                botState = BotState.SHOW_REMINDER;
                break;
            case HELP:
                botState = BotState.SHOW_HELP_MENU;
                break;
            case MY_LOCATION:
                botState = BotState.ASK_LOCATION;
                break;
            case BACK:
                botState = BotState.MENU_CHANGED;
                break;
            case ADD_NEW_REMINDER:
                botState = BotState.ASK_DATE;
                break;
            case SETTINGS:
                botState = BotState.MENU_CHANGED;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }
}

