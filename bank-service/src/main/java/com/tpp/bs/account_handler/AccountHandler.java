package com.tpp.bs.account_handler;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountHandler {

    private final AccountMapper accountMapper;
    private final AccountService accountService;

    public Boolean openAccount(AccountRequest accountOpenRequest) {
        return accountService.processAccountOpening(accountMapper.map(accountOpenRequest));
    }

    public Boolean processEndOfTheDayBalance(AccountBalanceCalculationRequest accountBalanceCalculationRequest) {
        if (Objects.isNull(accountBalanceCalculationRequest.getBalanceDate())) {
            log.error("Invalid request has been passed to process end of the day balance with balanceDate: {}", accountBalanceCalculationRequest.getBalanceDate());
            return Boolean.FALSE;
        }
        List<Account> accounts = accountBalanceCalculationRequest.getAccounts()
                .stream().map(accountMapper::map)
                .collect(Collectors.toList());
        return accountService.calculateDailyAccruedInterest(accounts, accountBalanceCalculationRequest.getBalanceDate());
    }
}
