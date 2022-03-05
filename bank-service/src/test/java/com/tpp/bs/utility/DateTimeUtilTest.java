package com.tpp.bs.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

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
}