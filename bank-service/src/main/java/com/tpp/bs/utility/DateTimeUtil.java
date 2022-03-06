package com.tpp.bs.utility;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static java.time.temporal.ChronoUnit.DAYS;

public class DateTimeUtil {
    public static int getDaysInMonth(LocalDate date) {
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
        return (int) DAYS.between(firstDayOfMonth, lastDayOfMonth) + 1;
    }

    public static boolean isLastDayOfMonth(OffsetDateTime currentDate) {
        LocalDate monthStartDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);
        LocalDate monthEndDate = monthStartDate.plusDays(monthStartDate.lengthOfMonth() - 1);
        return currentDate.toLocalDate().compareTo(monthEndDate) == 0;
    }
}
