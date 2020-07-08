package com.mdo.teleg_bot.botapi.handlers.askreminder;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.InputMessageHandler;
import com.mdo.teleg_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AskReminderHandler implements InputMessageHandler {
    private ReplyMessageService messageService;

    public AskReminderHandler(ReplyMessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public SendMessage handle(Message message) {
        return processUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_REMINDER;
    }

    private SendMessage processUserInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messageService.getReplyMessage(chatId, "reply.askReminder");
        replyToUser.setReplyMarkup(getInlineMessageButtons());
        //replyToUser.setReplyMarkup(M)

        return replyToUser;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = new InlineKeyboardButton().setText("Yes");
        InlineKeyboardButton buttonNo = new InlineKeyboardButton().setText("No");

        buttonYes.setCallbackData("buttonYes");
        buttonNo.setCallbackData("buttonNo");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonYes);
        keyboardButtonsRow1.add(buttonNo);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
