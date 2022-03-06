package com.tpp.bs.account;

public interface AccountCommandRepository {
    boolean saveAccount(Account account);

    boolean saveDailyAccountInterest(DailyInterest dailyInterest);

    void saveMonthlyInterest(MonthlyInterest monthlyInterest);

    void updateAccountWithMonthlyInterest(Account account, MonthlyInterest monthlyInterest);
}
