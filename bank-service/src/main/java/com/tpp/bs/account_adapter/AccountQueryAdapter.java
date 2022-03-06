package com.tpp.bs.account_adapter;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.AccountQueryRepository;
import com.tpp.bs.account.DailyInterest;
import com.tpp.bs.account.MonthlyInterest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountQueryAdapter implements AccountQueryRepository {

    private final AccountJpaRepository accountJpaRepository;
    private final DailyInterestJpaRepository dailyInterestJpaRepository;
    private final MonthlyInterestJpaRepository monthlyInterestJpaRepository;

    @Override
    public Optional<Account> findAccountBy(String identification) {
        try {
            return accountJpaRepository.findByIdentification(identification)
                    .map(accountEntity ->
                            Optional.of(Account.builder()
                                    .openingDate(accountEntity.getOpeningDate())
                                    .balance(accountEntity.getBalance())
                                    .bsb(accountEntity.getBsb())
                                    .identification(accountEntity.getIdentification())
                                    .lastInterest(accountEntity.getLastCalculatedInterest())
                                    .createdDate(accountEntity.getCreatedTime())
                                    .build())
                    )
                    .orElseGet(() -> {
                        log.error("No Account found for the given identification : {}", identification);
                        return Optional.empty();
                    });
        } catch (Exception e) {
            log.error("Exception occurred while fetching account {}", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<DailyInterest> findDailyInterestBy(String identification, LocalDate date) {
        try {
            return dailyInterestJpaRepository.findByIdIdentificationAndIdDate(identification, date)
                    .map(dailyInterestEntity ->
                            Optional.of(map(dailyInterestEntity))
                    )
                    .orElseGet(() -> {
                        log.error("No Daily Interest found for the given identification : {}, date {}", identification, date);
                        return Optional.empty();
                    });
        } catch (Exception e) {
            log.error("Exception occurred while fetching account {}", e);
            return Optional.empty();
        }
    }

    @Override
    public List<DailyInterest> findDailyInterestInMonth(String identification, LocalDate date) {
        try {
            return dailyInterestJpaRepository.findByYearAndMonth(identification, date.getYear(), date.getMonthValue())
                    .stream()
                    .map(this::map)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Exception occurred while fetching daily interests for the whole month {}", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<MonthlyInterest> findMonthlyInterestBy(String identification, OffsetDateTime dateTime) {
        try {
            return monthlyInterestJpaRepository.findByIdIdentificationAndIdYearAndIdMonth(identification, dateTime.getYear(), dateTime.getMonthValue())
                    .map(monthlyInterestEntity ->
                            Optional.of(MonthlyInterest.builder()
                                    .interest(monthlyInterestEntity.getInterest())
                                    .identification(monthlyInterestEntity.getId().getIdentification())
                                    .build())
                    ).orElseGet(() -> {
                        log.warn("Unable to find Monthly interest for month: {}, year: {}", dateTime.getYear(), dateTime.getMonthValue());
                        return Optional.empty();
                    });
        } catch (Exception e) {
            log.error("Exception occurred while fetching monthly interests for the whole month {}", e);
            return Optional.empty();
        }
    }

    private DailyInterest map(DailyInterestEntity dailyInterestEntity) {
        return DailyInterest.builder()
                .interest(dailyInterestEntity.getInterest())
                .localDateTime(dailyInterestEntity.getId().getDate())
                .dailyInterestRate(dailyInterestEntity.getInterestRate())
                .identification(dailyInterestEntity.getId().getIdentification())
                .build();
    }
}
