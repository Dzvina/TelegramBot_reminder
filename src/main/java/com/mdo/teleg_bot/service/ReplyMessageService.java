package com.mdo.teleg_bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ReplyMessageService {

    private LocalMessageService localMessageService;

    public ReplyMessageService(LocalMessageService messageService) {
        this.localMessageService = messageService;
    }

    public SendMessage getReplyMessage(long chat_id, String replyMessage) {
    return new SendMessage(chat_id,localMessageService.getMessage(replyMessage));
    }

    public SendMessage getReplyMessage(long chat_id, String replyMessage, Object...args){
        return new SendMessage(chat_id,localMessageService.getMessage(replyMessage, args));
    }

    public String getReplyText(String replyText){
        return localMessageService.getMessage(replyText);
    }
}
