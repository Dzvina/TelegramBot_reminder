package com.mdo.teleg_bot.botapi.handlers.greeting;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.InputMessageHandler;
import com.mdo.teleg_bot.service.MainMenuService;
import com.mdo.teleg_bot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class GreetingHandler implements InputMessageHandler {
    private MainMenuService mainMenuService;
    private ReplyMessageService messageService;

    public GreetingHandler(MainMenuService mainMenuService, ReplyMessageService messageService) {
        this.mainMenuService = mainMenuService;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {

        SendMessage replyToUser = messageService.getReplyMessage(message.getChatId(), "reply.greeting");
        replyToUser.setReplyMarkup(mainMenuService.getLocationMenuKeyboard());

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.GREETING;
    }
}
