package com.mdo.teleg_bot.appconfig;

import com.mdo.teleg_bot.MySuperBot;
import com.mdo.teleg_bot.botapi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

    private String botUserName;
    private String botToken;
    private String botPath;

    @Bean
    public MySuperBot MySuperTelegramBot(TelegramFacade telegramFacade) {
        ;
        MySuperBot mySuperBot = new MySuperBot( telegramFacade);

        mySuperBot.setBotUserName(botUserName);
        mySuperBot.setBotToken(botToken);
        mySuperBot.setBotPath(botPath);

        return mySuperBot;
    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:question");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;

    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
