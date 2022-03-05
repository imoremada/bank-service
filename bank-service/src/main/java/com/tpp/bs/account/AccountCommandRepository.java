package com.tpp.bs.account;

public interface AccountCommandRepository {
    boolean saveAccount(Account account);

    boolean saveDailyAccountInterest(DailyInterest dailyInterest);
}
