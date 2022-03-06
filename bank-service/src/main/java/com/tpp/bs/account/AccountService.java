package com.tpp.bs.account;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Boolean processAccountOpening(Account account);

    List<Account> calculateDailyAccruedInterest(List<Account> accounts, LocalDate balanceDate);

    Optional<Account> calculateMonthlyInterest(String identificaton);
}
