package com.tpp.bs.account_adapter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
@Setter
@SuperBuilder
@Table(name = "monthly_interest")
public class MonthlyInterestEntity {
    @EmbeddedId
    private MonthlyInterestId id;
    private BigDecimal interest;
    private LocalDate calculatedDate;
}
