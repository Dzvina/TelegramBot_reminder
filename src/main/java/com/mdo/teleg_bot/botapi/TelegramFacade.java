package com.mdo.teleg_bot.botapi;

import com.mdo.teleg_bot.botapi.handlers.calendar.Calendar;
import com.mdo.teleg_bot.botapi.handlers.fillingprofile.UserProfileData;
import com.mdo.teleg_bot.cache.UserDataCache;
import com.mdo.teleg_bot.client.TelegramRestClient;
import com.mdo.teleg_bot.client.TimeZoneRestClient;
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
            case "/start":
                botState = BotState.ASK_REMINDER;
                break;
            case "Create new reminder":
                botState = BotState.FILLING_PROFILE;
                break;
            case "My reminders":
                botState = BotState.SHOW_USER_PROFILE;
                break;
            case "Help":
                botState = BotState.SHOW_HELP_MENU;
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
        DeleteMessage deleteMessage = new DeleteMessage();


        //From reminder choose buttons
        if (buttonQuery.getData().equals("buttonYes")) {
            callBackAnswer = new SendMessage(chatId, "Write me your time zone. For example: +2");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_DATE);
        } else if (buttonQuery.getData().equals("buttonNo")) {
            callBackAnswer = sendAnswerCallbackQuery("Come back when you're ready", false, buttonQuery);
        }

        //From calendar choose month
        else if (buttonQuery.getData().contains("NEXT_MONTH")) {
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
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
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

