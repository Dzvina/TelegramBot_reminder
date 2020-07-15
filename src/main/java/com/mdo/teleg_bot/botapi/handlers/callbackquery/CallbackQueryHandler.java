package com.mdo.teleg_bot.botapi.handlers.callbackquery;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.BotStateContext;
import com.mdo.teleg_bot.botapi.handlers.calendar.Calendar;
import com.mdo.teleg_bot.cache.UserDataCache;
import com.mdo.teleg_bot.client.TelegramRestClient;
import com.mdo.teleg_bot.model.Reminder;
import com.mdo.teleg_bot.service.MainMenuService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.time.LocalDate;

@Component
public class CallbackQueryHandler {

    private final TelegramRestClient telegramRestClient;
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private MainMenuService mainMenuService;
    private Calendar calendar;

    public CallbackQueryHandler(BotStateContext botStateContext,
                                UserDataCache userDataCache,
                                MainMenuService mainMenuService,
                                Calendar calendar, TelegramRestClient telegramRestClient) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.calendar = calendar;
        this.telegramRestClient = telegramRestClient;
    }


    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        final int messadeId = buttonQuery.getMessage().getMessageId();
        SendMessage replyToUser;
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Use main menu");
        DeleteMessage deleteMessage;

        //From reminder choose buttons
        if (buttonQuery.getData().equals("buttonYes")) {
            replyToUser = new SendMessage(chatId, "Enter the reminder date. For example: DD/MM/YYYY or make your  choice using calendar");
            replyToUser.setReplyMarkup(calendar.getCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue()));
            callBackAnswer = replyToUser;
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TIME);
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

            replyToUser = new SendMessage(chatId, "Enter the reminder date. For example: DD/MM/YYYY or make your  choice using calendar");
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

            replyToUser = new SendMessage(chatId, "Enter the reminder date. For example: DD/MM/YYYY or make your  choice using calendar");
            replyToUser.setReplyMarkup(calendar.getCalendar(year, month));
            callBackAnswer = replyToUser;

        } else if (buttonQuery.getData().contains("DATE")) {
            String response = buttonQuery.getData();
            String[] responseElements = response.split(";");
            int year = new Integer(responseElements[1]);
            int month = new Integer(responseElements[2]);
            int day = new Integer(responseElements[3]);

            LocalDate localDateReminder = LocalDate.of(year, month, day);
            Reminder reminder = userDataCache.getReminder(userId);
            reminder.setDate(localDateReminder);
            userDataCache.saveReminder(userId, reminder);
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
