package com.tpp.bs.account;

public interface AccountCommandRepository {
    Boolean saveDailyInterest(Account account);

    void saveDailyInterest(DailyInterest dailyInterest);
}
