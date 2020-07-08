package com.mdo.teleg_bot.botapi.handlers.calendar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Calendar {
    private final static Integer FIRST_DAY = 1;
    public static final String EMPTY_FIELD = " ";
    public static final String IGNORE_THIS = "ignore_this";

    public InlineKeyboardMarkup getCalendar(int year, int month) {
        LocalDate localDate = LocalDate.of(year, month, FIRST_DAY);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> calendar = new ArrayList<>();

        calendar.add(getCurrentMonthYearRow(localDate));
        calendar.add(getDaysOfWeekRow());
        buildCalendarBody(localDate).stream().forEach(f -> calendar.add(f));
        calendar.add(getNavigationRow(year, month));

        inlineKeyboardMarkup.setKeyboard(calendar);
        return inlineKeyboardMarkup;
    }

    private List<List<InlineKeyboardButton>> buildCalendarBody(LocalDate monthsFirstDay) {
        int monthLength = monthsFirstDay.getMonth().length(monthsFirstDay.isLeapYear());
        int currentDay = 1;
        int currentDayOfWeek = monthsFirstDay.getDayOfWeek().getValue();
        int year = monthsFirstDay.getYear();
        int month = monthsFirstDay.getMonthValue();

        List<List<InlineKeyboardButton>> calendar = new ArrayList<>();
        while (currentDay <= monthLength) {
            List<InlineKeyboardButton> keyboardButtonsRows = new ArrayList<>();
            for (int k = 1; k < currentDayOfWeek; k++) {
                InlineKeyboardButton emptyButton = new InlineKeyboardButton(EMPTY_FIELD)
                        .setCallbackData(IGNORE_THIS);
                keyboardButtonsRows.add(emptyButton);
            }

            for (int i = currentDayOfWeek; i <= 7; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton()
                        .setText(Integer.toString(currentDay))
                        .setCallbackData(buildCallBackData(Action.DATE, year, month, currentDay));
                keyboardButtonsRows.add(button);
                currentDay++;
                if (currentDay > monthLength) {
                    for (int k = i + 1; k <= 7; k++) {
                        InlineKeyboardButton emptyButton = new InlineKeyboardButton(EMPTY_FIELD)
                                .setCallbackData(IGNORE_THIS);
                        keyboardButtonsRows.add(emptyButton);
                    }
                    break;
                }
            }
            currentDayOfWeek = 1;
            calendar.add(keyboardButtonsRows);
        }
        return calendar;
    }


    private List<InlineKeyboardButton> getNavigationRow(int year, int month) {
        InlineKeyboardButton buttonBack = new InlineKeyboardButton("<-")
                .setCallbackData(buildCallBackData(Action.PREVIOUS_MONTH, year, month, 1));
        InlineKeyboardButton buttonForward = new InlineKeyboardButton("->")
                .setCallbackData(buildCallBackData(Action.NEXT_MONTH, year, month, 1));

        List<InlineKeyboardButton> navigation = new ArrayList<>();
        navigation.add(buttonBack);
        navigation.add(buttonForward);

        return navigation;
    }

    private List<InlineKeyboardButton> getDaysOfWeekRow() {
        List<InlineKeyboardButton> keyboardButtonsDaysOfWeek = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            InlineKeyboardButton dayOfWeekButton = new InlineKeyboardButton(dayOfWeek.toString())
                    .setCallbackData(IGNORE_THIS);
            keyboardButtonsDaysOfWeek.add(dayOfWeekButton);
        }
        return keyboardButtonsDaysOfWeek;
    }

    private List<InlineKeyboardButton> getCurrentMonthYearRow(LocalDate monthsFirstDay) {
        InlineKeyboardButton currentYearMonth = new InlineKeyboardButton()
                .setText(monthsFirstDay.getMonth().name() + " " + monthsFirstDay.getYear())
                .setCallbackData(IGNORE_THIS);
        List<InlineKeyboardButton> rowYearsAndMonth = new ArrayList<>();
        rowYearsAndMonth.add(currentYearMonth);
        return rowYearsAndMonth;
    }


    private String buildCallBackData(Action action, int year, int month, int day) {
        return action.name() + ";" + year + ";" + month + ";" + day;
    }

    enum Action {
        DATE,
        NEXT_MONTH,
        PREVIOUS_MONTH;
    }

    enum DayOfWeek {
        Mo,
        Tu,
        We,
        Th,
        Fr,
        Sa,
        Su;
    }
}
