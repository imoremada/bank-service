package com.tpp.bs.account;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
public class MonthlyInterest {
    private BigDecimal interest;
    private String identification;
    private LocalDate interestDate;
}
