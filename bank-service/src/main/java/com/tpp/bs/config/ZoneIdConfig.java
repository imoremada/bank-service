package com.tpp.bs.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

@Configuration
@AllArgsConstructor
public class ZoneIdConfig {

    private ApplicationProperties applicationProperties;

    @Bean
    public ZoneId zoneId(){
        return ZoneId.of(applicationProperties.getZoneId());
    }
}
