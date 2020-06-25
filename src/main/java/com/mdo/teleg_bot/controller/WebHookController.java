package com.mdo.teleg_bot.controller;

import com.mdo.teleg_bot.MySuperBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


@RestController
public class WebHookController {
    private final MySuperBot mySuperBot;

    public WebHookController(MySuperBot mySuperBot) {
        this.mySuperBot = mySuperBot;
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return mySuperBot.onWebhookUpdateReceived(update);
    }
}
