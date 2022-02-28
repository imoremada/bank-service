package com.tpp.bs.account_handler;

import com.tpp.bs.account.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountMapper {

    public Account map(AccountOpenRequest accountOpenRequest){
        return Account.builder()
                .identification(accountOpenRequest.getIdentification())
                .bsb(accountOpenRequest.getBsb())
                .balance(BigDecimal.ZERO)
                .openingDate(accountOpenRequest.getOpeningDate())
                .build();
    }
}
