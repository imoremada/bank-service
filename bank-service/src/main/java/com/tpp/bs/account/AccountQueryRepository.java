package com.tpp.bs.account;

import java.time.LocalDate;
import java.util.Optional;

public interface AccountQueryRepository {
    Optional<Account> findAccountBy(String identification);

    Optional<DailyInterest> findDailyInterestBy(String identification, LocalDate date);
}
