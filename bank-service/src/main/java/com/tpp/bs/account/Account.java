package com.tpp.bs.account;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
public class Account {
    private String bsb;
    private String identification;
    private LocalDate openingDate;
    private BigDecimal balance;
}
