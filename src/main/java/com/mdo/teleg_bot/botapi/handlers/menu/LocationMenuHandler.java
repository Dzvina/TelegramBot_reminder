package com.mdo.teleg_bot.botapi.handlers.menu;

import com.mdo.teleg_bot.botapi.BotState;
import com.mdo.teleg_bot.botapi.InputMessageHandler;
import com.mdo.teleg_bot.client.TimeZoneRestClient;
import com.mdo.teleg_bot.client.TimezoneOffsetResponse;
import com.mdo.teleg_bot.dao.CityDao;
import com.mdo.teleg_bot.dao.UserDao;
import com.mdo.teleg_bot.model.City;
import com.mdo.teleg_bot.model.User;
import com.mdo.teleg_bot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.mdo.teleg_bot.botapi.handlers.inlinekeybordmenu.InlineKeyboardMenuHandler.getInlineMessageButtonsAddReminder;
import static com.mdo.teleg_bot.staticdata.Messages.MY_LOCATION;

@Component
public class LocationMenuHandler implements InputMessageHandler {

    final
    TimeZoneRestClient timeZoneRestClient;
    private final CityDao cityDao;
    private final UserDao userDao;
    private ReplyMessageService messageService;

    public LocationMenuHandler(ReplyMessageService messageService, TimeZoneRestClient timeZoneRestClient, CityDao cityDao, UserDao userDao) {
        this.messageService = messageService;
        this.timeZoneRestClient = timeZoneRestClient;
        this.cityDao = cityDao;
        this.userDao = userDao;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage replyToUser;
        User user = userDao.getUserByUserId(message.getFrom().getId());

        if (message.getText().equals(MY_LOCATION)) {
            replyToUser = messageService.getReplyMessage(message.getChatId(), "reply.askLocation");
        } else {
            City city = cityDao.getCityByCityName(message.getText());
            TimezoneOffsetResponse timezoneOffsetResponse = timeZoneRestClient.timeZone(city.getLatitude(), city.getLongitude());
            if(user != null){
                userDao.updateUserByUserId(message.getFrom().getId(),timezoneOffsetResponse.getTimezoneOffset());
            }else{
                System.out.println(city.getLongitude() + "     " + city.getLatitude());
                user = new User();
                user.setUserLocation(timezoneOffsetResponse.getTimezoneOffset());
                user.setUserId(message.getFrom().getId());
                userDao.insertUserLocation(user);
            }
            replyToUser = messageService.getReplyMessage(message.getChatId(), "reply.askReminder");
            replyToUser.setReplyMarkup(getInlineMessageButtonsAddReminder());
        }
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_LOCATION;
    }

}
