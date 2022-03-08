package com.tpp.bs.account_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountHandler accountHandler;

    @PostMapping("/processAccountOpening")
    public Boolean processAccountOpening(@Valid @RequestBody AccountDto accountOpenRequest) {
        return accountHandler.openAccount(accountOpenRequest);
    }

    @PutMapping("/processAccountEndOfDayBalances")
    public List<AccountDto> processEndOfTheDayBalance(@Valid @RequestBody AccountBalanceCalculationRequest accountBalanceCalculationRequest) {
        return accountHandler.processEndOfTheDayBalance(accountBalanceCalculationRequest);
    }

    @PutMapping("/calculateMonthlyInterest")
    public AccountDto calculateMonthlyInterest(@RequestParam String identification) {
        return accountHandler.calculateMonthlyInterest(identification);
    }
}
