package com.mdo.teleg_bot.botapi.handlers.greeting;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.InputMessageHandler;
import com.mdo.teleg_bot.cache.UserDataCache;
import com.mdo.teleg_bot.dao.UserDao;
import com.mdo.teleg_bot.model.User;
import com.mdo.teleg_bot.service.MainMenuService;
import com.mdo.teleg_bot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.mdo.teleg_bot.botapi.handlers.inlinekeybordmenu.InlineKeyboardMenuHandler.getInlineMessageButtons;

@Component
public class GreetingHandler implements InputMessageHandler {
    private UserDao userDao;
    private MainMenuService mainMenuService;
    private ReplyMessageService messageService;
    private UserDataCache userDataCache;

    public GreetingHandler(MainMenuService mainMenuService, ReplyMessageService messageService, UserDao userDao,
                           UserDataCache userDataCache) {
        this.mainMenuService = mainMenuService;
        this.messageService = messageService;
        this.userDao = userDao;
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        User user = userDao.getUserByUserId(message.getFrom().getId());
        SendMessage replyToUser;
        if (user != null) {
            replyToUser = messageService.getReplyMessage(message.getChatId(), "reply.greetingIfClientExist");
            replyToUser.setReplyMarkup(mainMenuService.getReminderMenuKeyboard());
        } else {
            replyToUser = messageService.getReplyMessage(message.getChatId(), "reply.greeting");
            replyToUser.setReplyMarkup(mainMenuService.getLocationMenuKeyboard());


        }
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.GREETING;
    }
}
