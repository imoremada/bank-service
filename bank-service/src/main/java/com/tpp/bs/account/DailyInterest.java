package com.tpp.bs.account;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
public class DailyInterest {
    private LocalDate localDateTime;
    private BigDecimal interest;
    private BigDecimal dailyInterestRate;
    private String identification;
}
