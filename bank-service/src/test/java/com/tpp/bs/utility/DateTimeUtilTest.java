package com.tpp.bs.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;

@ExtendWith(MockitoExtension.class)
class DateTimeUtilTest {

    @Test
    public void testShouldReturnNumberOfDaysInMonth(){
        //Given
        LocalDate localDate = LocalDate.of(2022, 3, 05);

        //When
        int daysInMonth = DateTimeUtil.getDaysInMonth(localDate);

        //Then
        Assertions.assertEquals(31,daysInMonth);
    }

    @Test
    public void testShouldReturnFalseIfDateIsNotTheLastDayOfTheMonth(){
        //Given
        LocalDate localDate = LocalDate.of(2022, 3, 30);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(1, 5));
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC);

        //When
        boolean lastDayOfMonth = DateTimeUtil.isLastDayOfMonth(offsetDateTime);

        //Then
        Assertions.assertFalse(lastDayOfMonth);
    }

    @Test
    public void testShouldReturnTrueForLastDayOfMonth(){
        //Given
        LocalDate localDate = LocalDate.of(2022, 3, 31);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(1, 5));
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC);

        //When
        boolean lastDayOfMonth = DateTimeUtil.isLastDayOfMonth(offsetDateTime);

        //Then
        Assertions.assertTrue(lastDayOfMonth);
    }
}