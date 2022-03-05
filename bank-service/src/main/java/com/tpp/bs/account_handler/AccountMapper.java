package com.tpp.bs.account_handler;

import com.tpp.bs.account.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class AccountMapper {

    public Account map(AccountRequest accountRequest){
        return Account.builder()
                .identification(accountRequest.getIdentification())
                .bsb(accountRequest.getBsb())
                .balance(Objects.isNull(accountRequest.getBalance()) ? BigDecimal.ZERO : accountRequest.getBalance())
                .openingDate(accountRequest.getOpeningDate())
                .build();
    }

    public AccountRequest map(Account account){
        return AccountRequest.builder()
                .balance(account.getBalance())
                .identification(account.getIdentification())
                .bsb(account.getBsb())
                .build();
    }
}
