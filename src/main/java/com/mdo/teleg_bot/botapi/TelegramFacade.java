package com.mdo.teleg_bot.botapi;

import com.mdo.teleg_bot.botapi.handlers.calendar.Calendar;
import com.mdo.teleg_bot.botapi.handlers.fillingprofile.UserProfileData;
import com.mdo.teleg_bot.cache.UserDataCache;
import com.mdo.teleg_bot.client.TelegramRestClient;
import com.mdo.teleg_bot.service.MainMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

import static com.mdo.teleg_bot.staticdata.Messages.*;

@Component
@Slf4j
public class TelegramFacade {


    private final TelegramRestClient telegramRestClient;
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private MainMenuService mainMenuService;
    private Calendar calendar;

    public TelegramFacade(BotStateContext botStateContext,
                          UserDataCache userDataCache,
                          MainMenuService mainMenuService,
                          Calendar calendar, TelegramRestClient telegramRestClient) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.calendar = calendar;
        this.telegramRestClient = telegramRestClient;
    }


    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
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
                botState = BotState.SHOW_USER_PROFILE;
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
                botState = BotState.FILLING_PROFILE;
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

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        final int messadeId = buttonQuery.getMessage().getMessageId();
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Use main menu");
        DeleteMessage deleteMessage;


        //From calendar choose month
        if (buttonQuery.getData().contains("NEXT_MONTH")) {
            String response = buttonQuery.getData();
            String[] responseElements = response.split(";");
            int year = new Integer(responseElements[1]);
            int month = new Integer(responseElements[2]);

            if (month == 12) {
                month = 1;
                year++;
            } else {
                month++;
            }
            deleteMessage = new DeleteMessage(String.valueOf(chatId), messadeId);
            telegramRestClient.deleteTelegramMessage(deleteMessage);

            SendMessage replyToUser = new SendMessage(chatId, "Enter the reminder date. For example: DD/MM/YYYY or make your  choice using calendar");
            replyToUser.setReplyMarkup(calendar.getCalendar(year, month));
            callBackAnswer = replyToUser;

        } else if (buttonQuery.getData().contains("PREVIOUS_MONTH")) {
            String response = buttonQuery.getData();
            String[] responseElements = response.split(";");
            int year = new Integer(responseElements[1]);
            int month = new Integer(responseElements[2]);

            if (month == 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            deleteMessage = new DeleteMessage(String.valueOf(chatId), messadeId);
            telegramRestClient.deleteTelegramMessage(deleteMessage);

            SendMessage replyToUser = new SendMessage(chatId, "Enter the reminder date. For example: DD/MM/YYYY or make your  choice using calendar");
            replyToUser.setReplyMarkup(calendar.getCalendar(year, month));
            callBackAnswer = replyToUser;

        } else if (buttonQuery.getData().contains("DATE")) {
            String response = buttonQuery.getData();
            String[] responseElements = response.split(";");
            int year = new Integer(responseElements[1]);
            int month = new Integer(responseElements[2]);
            int day = new Integer(responseElements[3]);

            LocalDate localDateReminder = LocalDate.of(year, month, day);
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setDate(localDateReminder);
            userDataCache.saveUserProfileData(userId, userProfileData);
            callBackAnswer = new SendMessage(chatId, "Enter the reminder time. For example: HH:MM");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_MESSAGE);

        } else {
            userDataCache.setUsersCurrentBotState(userId, BotState.MENU_CHANGED);
        }
        return callBackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }
}

