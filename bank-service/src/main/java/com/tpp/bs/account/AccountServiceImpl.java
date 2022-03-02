package com.tpp.bs.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountCommandRepository accountCommandRepository;

    @Override
    public Boolean processAccountOpening(Account account) {
        return accountCommandRepository.create(account);
    }

    @Override
    public Boolean calculateDailyAccruedInterest(List<Account> accounts, LocalDate balanceDate) {
        return Boolean.FALSE;
    }
}
