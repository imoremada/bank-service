package com.tpp.bs.account;

import java.time.LocalDate;
import java.util.List;

public interface AccountService {
    Boolean processAccountOpening(Account account);

    Boolean calculateDailyAccruedInterest(List<Account> accounts, LocalDate balanceDate);
}
