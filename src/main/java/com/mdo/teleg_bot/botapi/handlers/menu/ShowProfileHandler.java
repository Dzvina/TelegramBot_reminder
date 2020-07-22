package com.mdo.teleg_bot.botapi.handlers.menu;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.InputMessageHandler;
import com.mdo.teleg_bot.cache.UserDataCache;
import com.mdo.teleg_bot.client.TelegramRestClient;
import com.mdo.teleg_bot.dao.ReminderDao;
import com.mdo.teleg_bot.model.Reminder;
import com.mdo.teleg_bot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static com.mdo.teleg_bot.botapi.handlers.inlinekeybordmenu.InlineKeyboardMenuHandler.getInlineMessageButtonsEditReminder;

@Component
public class ShowProfileHandler implements InputMessageHandler {

    private final static String FINAL_MESSAGE = "-------------------%n" +
            "Date: %s%n" +
            "Time: %s%n" +
            "Message: %s%n";

    private UserDataCache userDataCache;
    private final ReminderDao reminderDao;
    private TelegramRestClient telegramRestClient;
    private ReplyMessageService messageService;

    public ShowProfileHandler(UserDataCache userDataCache, ReminderDao reminderDao,
                              TelegramRestClient telegramRestClient, ReplyMessageService messageService) {
        this.userDataCache = userDataCache;
        this.reminderDao = reminderDao;
        this.telegramRestClient = telegramRestClient;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {
        //userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_REMINDER);  //at this moment nothing to do!
        SendMessage replyToUser;
        List<Reminder> reminders = reminderDao.getAllRemindersByUserId(message.getFrom().getId());
        String responseString = "";

        for (int i = 0; i < reminders.size(); i++) {
            responseString = String.format(FINAL_MESSAGE,
                    reminders.get(i).getDate(),
                    reminders.get(i).getTime(),
                    reminders.get(i).getMessage());

            replyToUser = new SendMessage(message.getChatId(), responseString);
            replyToUser.setReplyMarkup(getInlineMessageButtonsEditReminder(reminders.get(i).getReminderId()));
            telegramRestClient.sendTelegramMessage(replyToUser);
        }
        return messageService.getReplyMessage(message.getChatId(),"reply.makeChoice");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_REMINDER;
    }
}
