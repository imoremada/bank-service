package com.tpp.bs.utility;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class DateTimeUtil {
    public static int getDaysInMonth(LocalDate date) {
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
        return (int) DAYS.between(firstDayOfMonth, lastDayOfMonth) + 1;
    }
}
