package com.tpp.bs.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
public class DateTimeProviderTest {

    @InjectMocks
    private DateTimeProvider dateTimeProvider;

    private ZoneId zoneId;

    @BeforeEach
    public void init() {
        zoneId = ZoneId.of("UTC");
    }

    @Test
    public void testShouldBeAbleToGetCurrentDateTime() {
        //Given
        zoneId = ZoneId.of("UTC");
        dateTimeProvider = new DateTimeProvider(zoneId);
        ZonedDateTime expectedDateTime= ZonedDateTime.now(zoneId);

        //When
        ZonedDateTime now = dateTimeProvider.now();

        //Then
        assertEquals(expectedDateTime.getYear(), now.getYear());
        assertEquals(expectedDateTime.getMonthValue(), now.getMonthValue());
        assertEquals(expectedDateTime.getDayOfMonth(), now.getDayOfMonth());
    }


    @Test
    public void testShouldBeAbleToGetCurrentOffsetDateTime() {
        //Given
        zoneId = ZoneId.of("UTC");
        dateTimeProvider = new DateTimeProvider(zoneId);
        ZonedDateTime expectedDateTime= ZonedDateTime.now(zoneId);

        //When
        OffsetDateTime now = dateTimeProvider.currentOffsetDateTime();

        //Then
        assertEquals(expectedDateTime.getYear(), now.getYear());
        assertEquals(expectedDateTime.getMonthValue(), now.getMonthValue());
        assertEquals(expectedDateTime.getDayOfMonth(), now.getDayOfMonth());
    }

    @Test
    public void testShouldBeAbleToGetCurrentDateTimeInString() {
        //Given
        dateTimeProvider = new DateTimeProvider(zoneId);

        //When
        String isoCurrentDateTime = dateTimeProvider.getIsoCurrentDateTime();

        //Then
        assertNotNull(isoCurrentDateTime);
    }
}