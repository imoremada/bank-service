package com.tpp.bs.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class DateTimeProvider {
    private static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final ZoneId zoneId;

    public ZonedDateTime now(){
        return ZonedDateTime.now(zoneId);
    }

    public OffsetDateTime currentOffsetDateTime(){
        return OffsetDateTime.now(zoneId);
    }

    public String getIsoCurrentDateTime(){
        return now().format(DateTimeFormatter.ofPattern(ISO_DATE_TIME_FORMAT));
    }
}