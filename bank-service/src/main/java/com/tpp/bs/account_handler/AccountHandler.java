package com.tpp.bs.account_handler;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountHandler {

    private final AccountMapper accountMapper;
    private final AccountService accountService;

    public Boolean openAccount(AccountDto accountOpenRequest) {
        return accountService.processAccountOpening(accountMapper.map(accountOpenRequest));
    }

    public List<AccountDto> processEndOfTheDayBalance(AccountBalanceCalculationRequest accountBalanceCalculationRequest) {
        if (Objects.isNull(accountBalanceCalculationRequest.getBalanceDate())) {
            log.error("Invalid request has been passed to process end of the day balance with balanceDate: {}", accountBalanceCalculationRequest.getBalanceDate());
            return Collections.EMPTY_LIST;
        }
        List<Account> accounts = accountBalanceCalculationRequest.getAccounts()
                .stream().map(accountMapper::map)
                .collect(Collectors.toList());
        return accountService.calculateDailyAccruedInterest(accounts, accountBalanceCalculationRequest.getBalanceDate())
                .stream().map(accountMapper::map)
                .collect(Collectors.toList());
    }

    public AccountDto calculateMonthlyInterest(String identification) {
        return accountService.calculateMonthlyInterest(identification).map(accountMapper::map)
                .orElseGet(() -> {
                    log.error("Failed to calculate monthly interest for identification : {}", identification);
                    return null;
                });
    }
}
