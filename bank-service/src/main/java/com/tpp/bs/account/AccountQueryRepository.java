package com.tpp.bs.account;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountQueryRepository {
    Optional<Account> findAccountBy(String identification);

    Optional<DailyInterest> findDailyInterestBy(String identification, LocalDate date);

    List<DailyInterest> findDailyInterestInMonth(String identification, LocalDate date);

    Optional<MonthlyInterest> findMonthlyInterestBy(String identification, OffsetDateTime currentDate);
}
