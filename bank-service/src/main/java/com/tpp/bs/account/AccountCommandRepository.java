package com.tpp.bs.account;

public interface AccountCommandRepository {
    boolean saveDailyInterest(Account account);

    boolean saveDailyInterest(DailyInterest dailyInterest);
}
