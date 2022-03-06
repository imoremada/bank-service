package com.tpp.bs.account_adapter;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.AccountQueryRepository;
import com.tpp.bs.account.DailyInterest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

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
    @OneToMany
    @MapsId("dailyInterestId")
    @JoinColumns({
            @JoinColumn(name="identification", referencedColumnName="identification")
    })
    private List<DailyInterestEntity> dailyInterests;

    @OneToMany
    @MapsId("monthlyInterestId")
    @JoinColumns({
            @JoinColumn(name="identification", referencedColumnName="identification")
    })
    private List<MonthlyInterestEntity> monthlyInterestEntities;
}
