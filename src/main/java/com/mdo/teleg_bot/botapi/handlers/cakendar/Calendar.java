package com.mdo.teleg_bot.botapi.handlers.cakendar;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Data
public class Calendar {
    private final static Integer FIRST_DAY = 1;
    private int month;
    private int year;

    public InlineKeyboardMarkup getInlineMessageButtons() {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        LocalDate localDate = LocalDate.now();
        year = localDate.getYear();
        month = localDate.getMonthValue();

        LocalDate monthsFirstDay = LocalDate.of(year, month, FIRST_DAY);

        int monthLength = monthsFirstDay.getMonth().length(monthsFirstDay.isLeapYear());
        int currentDay = 1;
        int currentDayOfWeek = monthsFirstDay.getDayOfWeek().getValue();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton currentYear = new InlineKeyboardButton(Integer.toString(year)).setCallbackData("currentYear");
        InlineKeyboardButton buttonBack = new InlineKeyboardButton("<-").setCallbackData("Back");
        InlineKeyboardButton buttonForward = new InlineKeyboardButton("->").setCallbackData("forward");

        List<InlineKeyboardButton> navigation = new ArrayList<>();
        navigation.add(buttonBack);
        navigation.add(buttonForward);

        List<InlineKeyboardButton> keyboardButtonsDayOfWeek = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            InlineKeyboardButton dayOfWeekButton = new InlineKeyboardButton(dayOfWeek.toString()).setCallbackData(" ");
            keyboardButtonsDayOfWeek.add(dayOfWeekButton);
        }

        List<List<InlineKeyboardButton>> calendar = new ArrayList<>();
        List<InlineKeyboardButton> rowYear = new ArrayList<>();
        rowYear.add(currentYear);
        calendar.add(rowYear);
        calendar.add(keyboardButtonsDayOfWeek);


        while (currentDay <= monthLength) {
            List<InlineKeyboardButton> keyboardButtonsRows = new ArrayList<>();
                for (int k = 1; k < currentDayOfWeek; k++) {
                    InlineKeyboardButton emptyButton = new InlineKeyboardButton(" ").setCallbackData(" ");
                    keyboardButtonsRows.add(emptyButton);
                }
            for (int i = currentDayOfWeek; i <= 7; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton().
                        setText(Integer.toString(currentDay)).setCallbackData(" ");
                keyboardButtonsRows.add(button);
                currentDay++;
                if (currentDay > monthLength) {
                    for (int k = i + 1; k <= 7; k++) {
                        InlineKeyboardButton emptyButton = new InlineKeyboardButton(" ").setCallbackData(" ");
                        keyboardButtonsRows.add(emptyButton);
                    }
                    break;
                }
            }
            currentDayOfWeek = 1;
            calendar.add(keyboardButtonsRows);
        }
        calendar.add(navigation);

        inlineKeyboardMarkup.setKeyboard(calendar);

        return inlineKeyboardMarkup;
    }
}
