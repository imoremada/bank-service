package com.tpp.bs.account_handler;

import com.tpp.bs.account.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class AccountMapper {

    public Account map(AccountDto accountDto){
        return Account.builder()
                .identification(accountDto.getIdentification())
                .bsb(accountDto.getBsb())
                .balance(Objects.isNull(accountDto.getBalance()) ? BigDecimal.ZERO : accountDto.getBalance())
                .openingDate(accountDto.getOpeningDate())
                .build();
    }

    public AccountDto map(Account account){
        return AccountDto.builder()
                .balance(account.getBalance())
                .identification(account.getIdentification())
                .bsb(account.getBsb())
                .build();
    }
}
