package com.tpp.bs.account_adapter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Entity
@Setter
@SuperBuilder
@Table(name = "account")
public class AccountEntity {
    @Id
    private String identification;
    private String bsb;
    private LocalDate openingDate;
    private BigDecimal balance;
    private OffsetDateTime lastUpdatedTime;
    private OffsetDateTime createdTime;
}
