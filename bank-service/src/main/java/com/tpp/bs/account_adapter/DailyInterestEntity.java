package com.tpp.bs.account_adapter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Entity
@Setter
@SuperBuilder
@Table(name = "daily_interest")
public class DailyInterestEntity {
    @EmbeddedId
    private DailyInterestId id;
    private BigDecimal interest;
    private BigDecimal interestRate;
}
