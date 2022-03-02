package com.tpp.bs.account_handler;

import com.tpp.bs.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountHandler {

    private final AccountMapper accountMapper;
    private final AccountService accountService;

    public Boolean openAccount(AccountRequest accountOpenRequest) {
        return accountService.processAccountOpening(accountMapper.map(accountOpenRequest));
    }

    public Boolean processEndOfTheDayBalance(AccountBalanceCalculationRequest accountBalanceCalculationRequest) {
        return Boolean.FALSE;
    }
}
