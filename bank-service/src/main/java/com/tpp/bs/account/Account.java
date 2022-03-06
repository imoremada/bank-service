package com.tpp.bs.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Builder
@Getter
public class Account {
    private String bsb;
    private String identification;
    private LocalDate openingDate;
    @Setter
    private OffsetDateTime createdDate;
    @Setter
    private BigDecimal balance;
    @Setter
    private BigDecimal lastInterest;
}
